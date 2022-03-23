package com.tokopedia.tokofood.purchase.purchasepage.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.LayoutFragmentPurchaseBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapter
import com.tokopedia.tokofood.purchase.purchasepage.view.adapter.TokoFoodPurchaseAdapterTypeFactory
import com.tokopedia.tokofood.purchase.purchasepage.view.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.view.subview.TokoFoodPurchaseGlobalErrorBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.view.subview.TokoFoodPurchaseNoteBottomSheet
import com.tokopedia.tokofood.purchase.purchasepage.view.toolbar.TokoFoodPurchaseToolbar
import com.tokopedia.tokofood.purchase.purchasepage.view.toolbar.TokoFoodPurchaseToolbarListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TokoFoodPurchaseFragment : BaseListFragment<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory>(),
        TokoFoodPurchaseActionListener, TokoFoodPurchaseToolbarListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var viewBinding by autoClearedNullable<LayoutFragmentPurchaseBinding>()
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TokoFoodPurchaseViewModel::class.java)
    }

    private var toolbar: TokoFoodPurchaseToolbar? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: TokoFoodPurchaseAdapter

    companion object {
        const val HAS_ELEVATION = 6
        const val NO_ELEVATION = 0

        const val REQUEST_CODE_CHANGE_ADDRESS = 111
        const val REQUEST_CODE_SET_PINPOINT = 112

        fun createInstance(): TokoFoodPurchaseFragment {
            return TokoFoodPurchaseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = LayoutFragmentPurchaseBinding.inflate(inflater, container, false)
        val view = viewBinding?.root
        recyclerView = getRecyclerView(view)
        (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        initializeToolbar(view)
        initializeRecyclerViewScrollListener()
        observeList()
        observeUiEvent()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No-op
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun getRecyclerViewResourceId() = R.id.recycler_view_purchase

    override fun initInjector() {
        activity?.let {
            DaggerTokoFoodPurchaseComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun loadData(page: Int) {
        showLoading()
        viewModel.loadData()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TokoFoodPurchaseAdapterTypeFactory> {
        adapter = TokoFoodPurchaseAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getAdapterTypeFactory(): TokoFoodPurchaseAdapterTypeFactory {
        return TokoFoodPurchaseAdapterTypeFactory(this)
    }

    override fun onBackPressed() {
        activity?.finish()
    }

    private fun initializeToolbar(view: View) {
        activity?.let {
            viewBinding?.toolbarPurchase?.removeAllViews()
            toolbar = TokoFoodPurchaseToolbar(it).apply {
                listener = this@TokoFoodPurchaseFragment
            }
            toolbar?.let {
                viewBinding?.toolbarPurchase?.addView(toolbar)
                (activity as AppCompatActivity).setSupportActionBar(viewBinding?.toolbarPurchase)
            }

            setToolbarShadowVisibility(false)
        }
    }

    private fun setToolbarShadowVisibility(show: Boolean) {
        if (show) {
            viewBinding?.appBarLayout?.elevation = HAS_ELEVATION.toFloat()
        } else {
            viewBinding?.appBarLayout?.elevation = NO_ELEVATION.toFloat()
        }
    }

    private fun setBackground() {
        activity?.let {
            it.window.decorView.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
    }

    private fun initializeRecyclerViewScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                // No-op
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.canScrollVertically(-1)) {
                    setToolbarShadowVisibility(true)
                } else {
                    setToolbarShadowVisibility(false)
                }
            }
        })
    }

    private fun observeList() {
        viewModel.visitables.observe(viewLifecycleOwner, {
            adapter.updateList(it)
        })
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner, {
            when (it.state) {
                UiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE -> renderGlobalError()
                UiEvent.EVENT_REMOVE_ALL_PRODUCT -> navigateToMerchantPage()
                UiEvent.EVENT_SUCCESS_REMOVE_PRODUCT -> onSuccessRemoveProduct(it.data as Int)
                UiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS -> scrollToIndex(it.data as Int)
                UiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG -> showBulkDeleteConfirmationDialog(it.data as Int)
                UiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT -> navigateToSetPinpoint(it.data as LocationPass)
            }
        })
    }

    private fun renderGlobalError() {

    }

    private fun navigateToSetPinpoint(locationPass: LocationPass) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle().apply {
            putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
    }

    private fun showBulkDeleteConfirmationDialog(productCount: Int) {
        activity?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Hapus $productCount item yang tidak bisa diproses?")
                setDescription("Semua item ini akan dihapus dari keranjangmu.")
                setPrimaryCTAText("Hapus")
                setSecondaryCTAText("Kembali")
                setPrimaryCTAClickListener {
                    viewModel.bulkDeleteUnavailableProducts()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
            }.show()
        }
    }

    private fun onSuccessRemoveProduct(productCount: Int) {
        view?.let {
            Toaster.build(
                    view = it,
                    text = "$productCount item berhasil dihapus.",
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_NORMAL,
                    actionText = "Oke"
            ).show()
        }
    }

    private fun navigateToMerchantPage() {
        // Todo : navigate to merchant page
        activity?.finish()
    }

    private fun scrollToIndex(index: Int) {
        activity?.let { activity ->
            recyclerView?.layoutManager?.let {
                val linearSmoothScroller = object : LinearSmoothScroller(activity) {
                    override fun getVerticalSnapPreference(): Int {
                        return SNAP_TO_START
                    }
                }
                linearSmoothScroller.targetPosition = index
                it.startSmoothScroll(linearSmoothScroller)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHANGE_ADDRESS -> onResultFromChangeAddress(resultCode, data)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        data?.let {
            val locationPass = it.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
            locationPass?.let {
                viewModel.updateAddressPinpoint()
            }
        }
    }

    private fun onResultFromChangeAddress(resultCode: Int, data: Intent?) {
        data?.let {
            val chosenAddressModel = it.getParcelableExtra(EXTRA_SELECTED_ADDRESS_DATA) as? ChosenAddressModel
            chosenAddressModel?.let {
                viewModel.updateAddress(it)
            }
        }
    }

    override fun getNextItems(currentIndex: Int, count: Int): List<Visitable<*>> {
        return viewModel.getNextItems(currentIndex, count)
    }

    override fun onTextChangeShippingAddressClicked() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_ADDRESS)
    }

    override fun onTextSetPinpointClicked() {
        viewModel.validateSetPinpoint()
    }

    override fun onTextAddItemClicked() {
        // Todo : navigate to merchant page
        view?.let {
            Toaster.build(it, "onTextAddItemClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onTextBulkDeleteUnavailableProductsClicked() {
        viewModel.validateBulkDelete()
    }

    override fun onQuantityChanged() {
        viewModel.calculateTotal()
    }

    override fun onIconDeleteProductClicked(element: TokoFoodPurchaseProductUiModel) {
        viewModel.deleteProduct(element.id)
    }

    override fun onTextChangeNotesClicked(element: TokoFoodPurchaseProductUiModel) {
        val addOnBottomSheet = TokoFoodPurchaseNoteBottomSheet(element.notes,
                object : TokoFoodPurchaseNoteBottomSheet.Listener {
                    override fun onSaveNotesClicked(notes: String) {
                        viewModel.updateNotes(element, notes)
                        view?.let {
                            Toaster.build(
                                    view = it,
                                    text = "Sip! Catatan berhasil disimpan.",
                                    duration = Toaster.LENGTH_SHORT,
                                    type = Toaster.TYPE_NORMAL,
                                    actionText = "Oke"
                            ).show()
                        }
                    }
                }
        )
        addOnBottomSheet.show(parentFragmentManager, "")
    }

    override fun onTextChangeNoteAndVariantClicked() {
        // Todo : navigate to edit variant page
        view?.let {
            Toaster.build(it, "onTextChangeNoteAndVariantClicked", Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel.toggleUnavailableProductsAccordion()
    }

    override fun onTextShowUnavailableItemClicked() {
        viewModel.scrollToUnavailableItem()
    }

    override fun onButtonCheckoutClicked() {
        // Todo : hit checkout API, validate response, show global error / toaster, reload
        val bottomSheet = TokoFoodPurchaseGlobalErrorBottomSheet(
                outOfService = null,
                listener = object : TokoFoodPurchaseGlobalErrorBottomSheet.Listener {
                    override fun onGoToHome() {

                    }

                    override fun onRetry() {

                    }

                    override fun onCheckOtherMerchant() {

                    }

                    override fun onStayOnCurrentPage() {

                    }
                }
        )
        bottomSheet.show(parentFragmentManager, "")
    }
}