package com.tokopedia.addongifting.view

import android.app.Activity
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.constant.ARGS_BBO_PROMO_CODES
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.feature.addongifting.data.AddOnProductData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
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
                    showCloseConfirmationDialog()
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
                showCloseConfirmationDialog()
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
        observeGlobalEvent()
        observeProductData()
        observeAddOnData()
        observeFragmentData(viewBinding)
    }

    private fun observeGlobalEvent() {
        viewModel.globalEvent.observe(this, {
            when (it.state) {
                GlobalEvent.STATE_FAILED_LOAD_ADD_ON_DATA -> {
                    // Todo : show global error
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
                // Todo : hit save state
                Toaster.build(viewBinding.bottomsheetContainer, "Will Save The State", Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL)
                        .show()
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
            it.removeErrorNetwork()
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