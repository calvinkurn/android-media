package com.tokopedia.notifcenter.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.inboxcommon.InboxFragment
import com.tokopedia.inboxcommon.InboxFragmentContainer
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.common.NotificationFilterType
import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel
import com.tokopedia.notifcenter.di.DaggerNotificationComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.listener.v3.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter
import com.tokopedia.notifcenter.presentation.adapter.decoration.NotificationItemDecoration
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactoryImpl
import com.tokopedia.notifcenter.presentation.fragment.bottomsheet.BottomSheetFactory
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.notifcenter.widget.NotificationFilterView
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.purchase_platform.common.constant.ATC_ONLY
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationFragment : BaseListFragment<Visitable<*>, NotificationTypeFactory>(),
        InboxFragment, NotificationItemListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var rv: VerticalRecyclerView? = null
    private var rvAdapter: NotificationAdapter? = null
    private var filter: NotificationFilterView? = null
    private var containerListener: InboxFragmentContainer? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(NotificationViewModel::class.java)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true
    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view
    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipe_refresh_layout
    override fun getScreenName(): String = "Notification"
    override fun getAdapterTypeFactory() = NotificationTypeFactoryImpl(this)
    override fun onItemClicked(t: Visitable<*>?) {}

    override fun onAttachActivity(context: Context?) {
        if (context is InboxFragmentContainer) {
            containerListener = context
        }
    }

    override fun loadData(page: Int) {
        viewModel.loadNotification(page, containerListener?.role)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)?.also {
            initView(it)
            setupObserver()
            setupRecyclerView()
            setupFilter()
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, NotificationTypeFactory> {
        rvAdapter = NotificationAdapter(adapterTypeFactory)
        return rvAdapter as NotificationAdapter
    }

    private fun initView(view: View) {
        rv = view.findViewById(R.id.recycler_view)
        filter = view.findViewById(R.id.sv_filter)
    }

    private fun setupObserver() {
        viewModel.notificationItems.observe(viewLifecycleOwner, Observer {
            if (it is Success) {
                renderList(it.data, false)
            }
        })
    }

    private fun setupRecyclerView() {
        val itemDecoration = NotificationItemDecoration(context)
        rv?.clearItemDecoration()
        rv?.addItemDecoration(itemDecoration)
    }

    private fun setupFilter() {
        filter?.filterListener = object : NotificationFilterView.FilterListener {
            override fun onFilterChanged(@NotificationFilterType filterType: Int) {
                viewModel.filter = filterType
                loadInitialData()
            }
        }
    }

    override fun onRoleChanged(role: Int) {
        loadInitialData()
    }

    override fun initInjector() {
        DaggerNotificationComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .commonModule(context?.let { CommonModule(it) })
                .build()
                .inject(this)
    }

    override fun showLongerContent(element: NotificationUiModel) {
        BottomSheetFactory.showLongerContent(childFragmentManager, element)
    }

    override fun showProductBottomSheet(element: NotificationUiModel) {
        BottomSheetFactory.showProductBottomSheet(childFragmentManager, element)
    }

    override fun buyProduct(product: ProductData) {
        val atcPageIntent = getBuyPageIntent(product)
        startActivity(atcPageIntent)
    }

    override fun addProductToCart(product: ProductData) {
        val atcPageIntent = getAtcPageIntent(product)
        startActivityForResult(atcPageIntent, REQUEST_CHECKOUT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECKOUT -> onReturnFromCheckout(resultCode, data)
        }
    }

    private fun onReturnFromCheckout(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK || data == null) return
        val message = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
                ?: return
        view?.let {
            Toaster.build(
                    it,
                    message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.notifcenter_title_view),
                    onClickSeeButtonOnAtcSuccessToaster()
            ).show()
        }
    }

    private fun onClickSeeButtonOnAtcSuccessToaster(): View.OnClickListener {
        return View.OnClickListener {
            RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
        }
    }

    private fun getBuyPageIntent(product: ProductData): Intent {
        val atcAndBuyAction = ATC_AND_BUY
        val needRefresh = true
        return RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT
        ).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shop.id.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, product.minOrder)
            putExtra(
                    ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID,
                    product.productId.toString()
            )
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, product.shop.name)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, product.name)
        }
    }

    private fun getAtcPageIntent(product: ProductData): Intent {
        val atcOnly = ATC_ONLY
        val needRefresh = true
        return RouteManager.getIntent(
                context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT
        ).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shop.id.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId.toString())
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, product.minOrder)
            putExtra(
                    ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID,
                    product.productId.toString()
            )
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcOnly)
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, product.shop.name)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
        }
    }

    companion object {
        private const val REQUEST_CHECKOUT = 0
    }
}