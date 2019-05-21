package com.tokopedia.checkout.view.feature.emptycart2

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.checkout.view.feature.emptycart2.di.DaggerEmptyCartComponent
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveView
import com.tokopedia.checkout.view.compoundview.ToolbarRemoveWithBackView
import com.tokopedia.checkout.view.feature.emptycart2.adapter.EmptyCartAdapterTypeFactory

/**
 * Created by Irfan Khoirul on 2019-05-17.
 */

class EmptyCartFragment : BaseListFragment<Visitable<*>, EmptyCartAdapterTypeFactory>() {

    private var toolbar: View? = null

    companion object {
        val ARG_AUTO_APPLY_MESSAGE = "ARG_AUTO_APPLY_MESSAGE"
        val ARG_AUTO_APPLY_STATE = "ARG_AUTO_APPLY_STATE"
        val ARG_AUTO_APPLY_TITLE = "ARG_AUTO_APPLY_TITLE"
        val ARG_AUTO_APPLY_PROMO_CODE = "ARG_AUTO_APPLY_PROMO_CODE"

        fun newInstance(autoApplyMessage: String?, args: String?, state: String?, titleDesc: String?, promoCode: String?): EmptyCartFragment {
            val emptyCartFragment = EmptyCartFragment()
            val bundle = Bundle()
            bundle.putString(EmptyCartFragment::class.java.simpleName, args)
            bundle.putString(ARG_AUTO_APPLY_MESSAGE, autoApplyMessage)
            bundle.putString(ARG_AUTO_APPLY_STATE, state)
            bundle.putString(ARG_AUTO_APPLY_TITLE, titleDesc)
            bundle.putString(ARG_AUTO_APPLY_PROMO_CODE, promoCode)
            emptyCartFragment.arguments = bundle

            return emptyCartFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_empty_cart_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
    }

    override fun getAdapterTypeFactory(): EmptyCartAdapterTypeFactory {
        return EmptyCartAdapterTypeFactory()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        // No op
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerEmptyCartComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun loadData(page: Int) {

    }

    private fun setupToolbar(view: View) {
        val appbar = view.findViewById<Toolbar>(R.id.toolbar)
        val statusBarBackground = view.findViewById<View>(R.id.status_bar_bg)
        statusBarBackground.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(activity)
        val args = arguments?.getString(EmptyCartFragment::class.java.simpleName)
        if (args != null && !args.isEmpty()) {
            toolbar = toolbarRemoveView()
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarBackground.visibility = View.INVISIBLE
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarBackground.visibility = View.VISIBLE
            } else {
                statusBarBackground.visibility = View.GONE
            }
        } else {
            toolbar = toolbarRemoveWithBackView()
            statusBarBackground.visibility = View.GONE
        }
        appbar.addView(toolbar)
        (getActivity() as AppCompatActivity).setSupportActionBar(appbar)
        setVisibilityRemoveButton(false)
    }

    private fun toolbarRemoveWithBackView(): ToolbarRemoveWithBackView {
        val toolbar = ToolbarRemoveWithBackView(activity!!)
        toolbar.navigateUp(activity)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun toolbarRemoveView(): ToolbarRemoveView {
        val toolbar = ToolbarRemoveView(activity!!)
        toolbar.setTitle(getString(R.string.cart))
        return toolbar
    }

    private fun setVisibilityRemoveButton(state: Boolean) {
        if (toolbar != null) {
            if (toolbar is ToolbarRemoveView) {
                (toolbar as ToolbarRemoveView).setVisibilityRemove(state)
            } else if (toolbar is ToolbarRemoveWithBackView) {
                (toolbar as ToolbarRemoveWithBackView).setVisibilityRemove(state)
            }
        }
    }

}