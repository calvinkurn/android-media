package com.tokopedia.sellerorder.common.presenter.fragments

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.databinding.FragmentSomContainerBinding
import com.tokopedia.sellerorder.detail.presentation.fragment.tablet.SomDetailFragment
import com.tokopedia.sellerorder.list.presentation.fragments.tablet.SomListFragment
import com.tokopedia.utils.view.binding.noreflection.viewBinding

class SomContainerFragment : TkpdBaseV4Fragment(), SomListFragment.SomListClickListener, SomDetailFragment.SomOrderDetailListener {
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
                    putString(SomConsts.FILTER_ORDER_TYPE, bundle.getString(SomConsts.FILTER_ORDER_TYPE))
                }
            }
        }

        private const val URL_WELCOME_ILLUSTRATION = TokopediaImageUrl.URL_WELCOME_ILLUSTRATION
    }

    private var binding by viewBinding(FragmentSomContainerBinding::bind)

    private var somListFragment: SomListFragment? = null
    private var somDetailFragment: SomDetailFragment? = null

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        attachFragments()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        somListFragment?.onActivityResult(requestCode, resultCode, data)
        somDetailFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onFragmentBackPressed(): Boolean {
        var handled = false
        somListFragment?.let { handled = it.onFragmentBackPressed() }
        somDetailFragment?.let { handled = handled || it.onFragmentBackPressed() }
        return handled
    }

    override fun onOrderClicked(orderId: String) {
        attachDetailFragment(orderId, false)
    }

    override fun closeOrderDetail() {
        binding?.run {
            somDetailFragment?.closeOrderDetail()
            fragmentDetail.gone()
            ivSomDetailWelcomeIllustration.show()
            tvSomDetailWelcome.show()
        }
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

    private fun setupViews() {
        binding?.ivSomDetailWelcomeIllustration?.loadImage(URL_WELCOME_ILLUSTRATION)
    }

    private fun attachFragments() {
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

    private fun initiateListFragment() {
        if (!isAdded) return
        somListFragment = somListFragment
                ?: childFragmentManager.findFragmentByTag(SomListFragment::class.java.simpleName) as? SomListFragment
                        ?: SomListFragment.newInstance(arguments ?: Bundle.EMPTY)
        somListFragment?.apply {
            setSomListOrderListener(this@SomContainerFragment)
        }
    }

    private fun initiateDetailFragment(orderId: String, passOrderDetail: Boolean): SomDetailFragment {
        val somDetailFragment = this.somDetailFragment
                ?: childFragmentManager.findFragmentByTag(SomListFragment::class.java.simpleName) as? SomDetailFragment
                ?: SomDetailFragment.newInstance(Bundle().apply {
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
                    .commitAllowingStateLoss()
        }
    }

    private fun attachDetailFragment(orderId: String, passOrderDetail: Boolean) {
        binding?.run {
            if (somDetailFragment == null) {
                if (!isAdded) return
                initiateDetailFragment(orderId, passOrderDetail).let {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.fragmentDetail, it, it::class.java.simpleName)
                        .commitAllowingStateLoss()
                }
            } else {
                somDetailFragment?.setOrderIdToShow(orderId)
            }
            fragmentDetail.show()
            ivSomDetailWelcomeIllustration.gone()
            tvSomDetailWelcome.gone()
        }
    }
}