package com.tokopedia.addongifting.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.LayoutAddOnBottomSheetBinding
import com.tokopedia.addongifting.view.adapter.AddOnListAdapter
import com.tokopedia.addongifting.view.adapter.AddOnListAdapterTypeFactory
import com.tokopedia.addongifting.view.di.AddOnComponent
import com.tokopedia.addongifting.view.di.DaggerAddOnComponent
import com.tokopedia.addongifting.view.uimodel.AddOnUiModel
import com.tokopedia.addongifting.view.uimodel.FragmentUiModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AddOnBottomSheet(val addOnProductData: AddOnProductData) : BottomSheetUnify(), AddOnActionListener, HasComponent<AddOnComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var adapter: AddOnListAdapter? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddOnViewModel::class.java)
    }

    private var viewBinding: LayoutAddOnBottomSheetBinding? = null
    private var measureRecyclerViewPaddingDebounceJob: Job? = null

    override fun getComponent(): AddOnComponent {
        return DaggerAddOnComponent
                .builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = initializeView()
        this.viewBinding = viewBinding

        initializeObserver(viewBinding)
        initializeData(addOnProductData)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            return object : BottomSheetDialog(it, theme) {
                override fun onBackPressed() {
                    viewModel.validateCloseAction()
                }
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initializeView(): LayoutAddOnBottomSheetBinding {
        val viewBinding = LayoutAddOnBottomSheetBinding.inflate(LayoutInflater.from(context))
        initializeBottomSheet(viewBinding)
        initializeRecyclerView(viewBinding)

        return viewBinding
    }

    private fun initializeBottomSheet(viewBinding: LayoutAddOnBottomSheetBinding) {
        setTitle("Atur pelengkap barang")
        showCloseIcon = true
        showHeader = true
        isDragable = true
        isHideable = true
        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        setShowListener {
            bottomSheetClose.setOnClickListener {
                viewModel.validateCloseAction()
            }
        }
        setChild(viewBinding.root)
    }

    private fun initializeRecyclerView(viewBinding: LayoutAddOnBottomSheetBinding) {
        val adapterTypeFactory = AddOnListAdapterTypeFactory(this)
        adapter = AddOnListAdapter(adapterTypeFactory)
        viewBinding.rvAddOn.adapter = adapter
        viewBinding.rvAddOn.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initializeData(addOnProductData: AddOnProductData) {
        val mockAddOnResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_response)
        val mockAddOnSavedStateResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_add_on_saved_state_response)
        showLoading()
        viewModel.loadAddOnData(addOnProductData, mockAddOnResponse, mockAddOnSavedStateResponse)
    }

    private fun initializeObserver(viewBinding: LayoutAddOnBottomSheetBinding) {
        observeGlobalEvent(viewBinding)
        observeProductData()
        observeAddOnData()
        observeFragmentData(viewBinding)
    }

    private fun observeGlobalEvent(viewBinding: LayoutAddOnBottomSheetBinding) {
        viewModel.globalEvent.observe(this, {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_LOAD_ADD_ON_DATA -> {
                    hideLoading()
                    renderLayoutNormal(viewBinding)
                }
                GlobalEvent.STATE_FAILED_LOAD_ADD_ON_DATA -> {
                    hideLoading()
                    renderLayoutError(viewBinding, it.throwable)
                }
                GlobalEvent.STATE_SHOW_CLOSE_DIALOG_CONFIRMATION -> {
                    showCloseConfirmationDialog()
                }
                GlobalEvent.STATE_SUCCESS_SAVE_ADD_ON -> {
                    // Todo : return result & dismiss bottom sheet
                    dismiss()
                }
                GlobalEvent.STATE_FAILED_SAVE_ADD_ON -> {
                    viewBinding.totalAmount.amountCtaView.isLoading = false
                    Toaster.build(viewBinding.bottomsheetContainer, "Gagal menyimpan pelengkap. Silakan coba lagi.", Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
                            .show()
                }
                GlobalEvent.STATE_DISMISS_BOTTOM_SHEET -> {
                    dismiss()
                }
            }
        })
    }

    private fun observeProductData() {
        viewModel.productUiModel.observe(this, {
            hideLoading()
            addOrModify(it)
        })
    }

    private fun observeAddOnData() {
        viewModel.addOnUiModel.observe(this, {
            hideLoading()
            addOrModify(it)
        })
    }

    private fun observeFragmentData(viewBinding: LayoutAddOnBottomSheetBinding) {
        viewModel.fragmentUiModel.observe(this, {
            renderTotalAmount(viewBinding, it)
        })
    }

    private fun renderTotalAmount(viewBinding: LayoutAddOnBottomSheetBinding, fragmentUiModel: FragmentUiModel) {
        with(viewBinding.totalAmount) {
            setLabelTitle("Total Pelengkap")
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentUiModel.addOnTotalPrice, false).removeDecimalSuffix())
            if (fragmentUiModel.addOnTotalQuantity > 0) {
                setCtaText("Simpan (${fragmentUiModel.addOnTotalQuantity})")
            } else {
                setCtaText("Simpan")
            }
            amountCtaView.show()
            amountCtaView.setOnClickListener {
                val mockSaveAddOnStateResponse = GraphqlHelper.loadRawString(context?.resources, R.raw.dummy_save_add_on_state_response)
                viewBinding.totalAmount.amountCtaView.isLoading = true
                viewModel.saveAddOnState(mockSaveAddOnStateResponse)
            }
        }
    }

    private fun addOrModify(it: Visitable<*>) {
        if (adapter?.data?.contains(it) == true) {
            adapter?.modifyData(adapter?.data?.indexOf(it) ?: RecyclerView.NO_POSITION)
        } else {
            adapter?.addVisitable(it)
        }
    }

    private fun showLoading() {
        adapter?.let {
            it.setLoadingModel(LoadingModel())
            it.showLoading()
        }
        viewBinding?.totalAmount?.isTotalAmountLoading = true
    }

    private fun hideLoading() {
        adapter?.hideLoading()
        viewBinding?.totalAmount?.isTotalAmountLoading = false
    }

    private fun adjustRecyclerViewPaddingBottom(viewBinding: LayoutAddOnBottomSheetBinding) {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        measureRecyclerViewPaddingDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(200L)
            adjustRecyclerViewPadding(viewBinding)
        }
    }

    private fun adjustRecyclerViewPadding(viewBinding: LayoutAddOnBottomSheetBinding) {
        with(viewBinding) {
            if (rvAddOn.canScrollVertically(-1) || rvAddOn.canScrollVertically(1) || isBottomSheetFullPage(viewBinding)) {
                rvAddOn.setPadding(0, 0, 0, rvAddOn.resources?.getDimensionPixelSize(R.dimen.dp_64)
                        ?: 0)
            } else {
                rvAddOn.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun isBottomSheetFullPage(viewBinding: LayoutAddOnBottomSheetBinding): Boolean {
        val displayMetrics = Resources.getSystem().displayMetrics
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val bottomSheetHeight = (bottomSheetWrapper.parent as? View)?.height ?: 0
        val recyclerViewPaddingBottom = viewBinding.rvAddOn.resources?.getDimensionPixelSize(R.dimen.dp_64)
                ?: 0
        val displayHeight = displayMetrics?.heightPixels ?: 0
        return bottomSheetHeight != 0 && displayHeight != 0 && (bottomSheetHeight + (recyclerViewPaddingBottom / 2)) >= displayHeight
    }

    private fun renderLayoutNormal(viewBinding: LayoutAddOnBottomSheetBinding) {
        with(viewBinding) {
            globalError.gone()
            totalAmount.show()
            rvAddOn.show()
        }
    }

    private fun renderLayoutError(viewBinding: LayoutAddOnBottomSheetBinding, throwable: Throwable?) {
        with(viewBinding) {
            throwable?.let {
                globalError.setType(getGlobalErrorType(throwable))
                globalError.setActionClickListener {
                    renderLayoutNormal(viewBinding)
                    initializeData(addOnProductData)
                }
            }
            globalError.show()
            totalAmount.gone()
            rvAddOn.gone()
        }
    }

    private fun getGlobalErrorType(throwable: Throwable): Int {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException || throwable is ConnectException) {
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun showCloseConfirmationDialog() {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Batal tambah pelengkap?")
                setDescription("Pesan kartu ucapan nggak akan tersimpan kalau kamu keluar dari sini.")
                setPrimaryCTAText("Ya, Batal")
                setSecondaryCTAText("Tetap di Sini")
                setPrimaryCTAClickListener {
                    this@AddOnBottomSheet.dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewBinding = null
        measureRecyclerViewPaddingDebounceJob?.cancel()
        activity?.finish()
        activity?.overridePendingTransition(android.R.anim.fade_in, R.anim.push_down)
        super.onDismiss(dialog)
    }

    override fun onCheckBoxCheckedChanged(addOnUiModel: AddOnUiModel) {
        viewBinding?.let {
//            adjustRecyclerViewPaddingBottom(it)
        }
        viewModel.updateFragmentUiModel(addOnUiModel)
    }

    override fun onAddOnImageClicked(addOnUiModel: AddOnUiModel) {
        val intent = Intent(context, AddOnGalleryActivity::class.java)
        val addOnImages = arrayListOf<String>()
        addOnImages.addAll(addOnUiModel.addOnAllImageUrls)
        intent.putStringArrayListExtra("ADD_ON_IMAGES", addOnImages)
        startActivity(intent)
    }
}