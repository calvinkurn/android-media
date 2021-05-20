package com.tokopedia.sellerorder.common.presenter.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.detail.presentation.fragment.tablet.SomDetailFragment
import com.tokopedia.sellerorder.list.presentation.fragments.tablet.SomListFragment
import kotlinx.android.synthetic.main.fragment_som_container.*

class SomContainerFragment : Fragment(), SomListFragment.SomListClickListener, SomDetailFragment.SomOrderDetailListener {
    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): SomContainerFragment {
            return SomContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(SomConsts.FILTER_STATUS_ID, bundle.getString(SomConsts.FILTER_STATUS_ID))
                    putBoolean(SomConsts.FROM_WIDGET_TAG, bundle.getBoolean(SomConsts.FROM_WIDGET_TAG))
                    putString(SomConsts.TAB_ACTIVE, bundle.getString(SomConsts.TAB_ACTIVE))
                    putString(SomConsts.TAB_STATUS, bundle.getString(SomConsts.TAB_STATUS))
                    putString(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH, bundle.getString(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH))
                    putString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID, bundle.getString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID))
                    putInt(SomConsts.FILTER_ORDER_TYPE, bundle.getInt(SomConsts.FILTER_ORDER_TYPE))
                }
            }
        }
    }

    private var somListFragment: SomListFragment? = null
    private var somDetailFragment: SomDetailFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachFragments()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        somListFragment?.onActivityResult(requestCode, resultCode, data)
        somDetailFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOrderClicked(orderId: String) {
        attachDetailFragment(orderId, false)
    }

    override fun closeOrderDetail() {
        somDetailFragment?.closeOrderDetail()
        fragmentDetail?.gone()
        ivSomDetailWelcomeIllustration?.show()
        tvSomDetailWelcome?.show()
    }

    override fun onRefreshSelectedOrder(orderId: String) {
        somDetailFragment?.refreshOrder(orderId)
    }

    override fun onRefreshOrder(orderId: String) {
        somListFragment?.refreshSelectedOrder(orderId)
    }

    override fun onShouldPassInvoice(invoice: String) {
        somListFragment?.applySearchParam(invoice)
    }

    private fun setupSomListWidth(fragmentList: FrameLayout?) {
        fragmentList?.let {
            val layoutParamCopy = it.layoutParams
            layoutParamCopy.width = it.width
            it.layoutParams = layoutParamCopy
        }
    }

    private fun attachFragments() {
        fragmentList.post {
            setupSomListWidth(fragmentList)
            initiateListFragment()
            attachListFragment()
            arguments?.let {
                it.getString(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID).let { orderId ->
                    if (!orderId.isNullOrEmpty()) {
                        attachDetailFragment(orderId, true)
                    }
                }
            }
        }
    }

    private fun initiateListFragment() {
        somListFragment = somListFragment ?: childFragmentManager.findFragmentByTag(SomListFragment::class.java.simpleName) as? SomListFragment ?: SomListFragment.newInstance(arguments ?: Bundle.EMPTY)
        somListFragment?.apply {
            setSomListOrderListener(this@SomContainerFragment)
        }
    }

    private fun initiateDetailFragment(orderId: String, passOrderDetail: Boolean): SomDetailFragment {
        val somDetailFragment = this.somDetailFragment ?: childFragmentManager.findFragmentByTag(SomListFragment::class.java.simpleName) as? SomDetailFragment ?: SomDetailFragment.newInstance(Bundle().apply {
            putString(SomConsts.PARAM_ORDER_ID, orderId)
            putBoolean(SomConsts.PARAM_PASS_INVOICE, passOrderDetail)
        })
        somDetailFragment.apply {
            setOrderDetailListener(this@SomContainerFragment)
        }
        this.somDetailFragment = somDetailFragment
        return somDetailFragment
    }

    private fun attachListFragment() {
        somListFragment?.let {
            if (!isAdded) return
            childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentList, it, it::class.java.simpleName)
                    .commit()
        }
    }

    private fun attachDetailFragment(orderId: String, passOrderDetail: Boolean) {
        if (somDetailFragment == null) {
            initiateDetailFragment(orderId, passOrderDetail).let {
                if (!isAdded) return
                childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentDetail, it, it::class.java.simpleName)
                        .commit()
            }
        } else {
            somDetailFragment?.setOrderIdToShow(orderId)
        }
        fragmentDetail?.show()
        ivSomDetailWelcomeIllustration?.gone()
        tvSomDetailWelcome?.gone()
    }
}