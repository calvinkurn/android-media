package com.tokopedia.expresscheckout.view.profile

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.profile.adapter.CheckoutProfileAdapter
import com.tokopedia.expresscheckout.view.profile.di.DaggerCheckoutProfileComponent
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel
import com.tokopedia.transactionanalytics.ExpressCheckoutAnalyticsTracker
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfileBottomSheet : BottomSheets(), CheckoutProfileContract.View, CheckoutProfileActionListener {

    @Inject
    lateinit var presenter: CheckoutProfileContract.Presenter
    @Inject
    lateinit var analytics: ExpressCheckoutAnalyticsTracker
    @Inject
    lateinit var adapter: CheckoutProfileAdapter

    private lateinit var listener: CheckoutProfileFragmentListener
    private lateinit var tvContinueWithoutTemplate: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var rvProfile: RecyclerView
    private lateinit var llNetworkErrorView: LinearLayout

    companion object {
        val ARGUMENT_DEFAULT_PROFILE_VIEW_MODEL = "ARGUMENT_DEFAULT_PROFILE_VIEW_MODEL"

        fun newInstance(): CheckoutProfileBottomSheet {
            return CheckoutProfileBottomSheet()
        }
    }

    public fun setListener(listener: CheckoutProfileFragmentListener) {
        this.listener = listener
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_bottomsheet_profile_list
    }

    override fun title(): String {
        return getString(R.string.title_choose_other_template)
    }

    public fun updateArguments(profileViewModel: com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel?) {
        val bundle = Bundle()
        bundle.putParcelable(ARGUMENT_DEFAULT_PROFILE_VIEW_MODEL, profileViewModel)
        arguments = bundle
    }

    private fun initInjector() {
        activity?.let {
            val baseAppComponent = it.application
            if (baseAppComponent is BaseMainApplication) {
                DaggerCheckoutProfileComponent.builder()
                        .baseAppComponent(baseAppComponent.baseAppComponent)
                        .build()
                        .inject(this)
            }
        }
    }

    override fun initView(view: View?) {
        initInjector()

        tvContinueWithoutTemplate = view?.findViewById<View>(R.id.tv_continue_without_template) as TextView
        pbLoading = view.findViewById<View>(R.id.pb_loading) as ProgressBar
        rvProfile = view.findViewById<View>(R.id.rv_profile) as RecyclerView
        llNetworkErrorView = view.findViewById<View>(R.id.ll_network_error_view) as LinearLayout
        tvContinueWithoutTemplate.setOnClickListener {
            for (profileViewModel: ProfileViewModel in adapter.data) {
                profileViewModel.isSelected = false
            }
            listener.onContinueWithoutProfile()
        }
        rvProfile.adapter = adapter
        rvProfile.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        adapter.setListener(this)
        presenter.attachView(this)
        presenter.loadData()
    }

    override fun showLoading() {
        tvContinueWithoutTemplate.visibility = View.GONE
        rvProfile.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
        rvProfile.visibility = View.VISIBLE
        tvContinueWithoutTemplate.visibility = View.VISIBLE
    }

    override fun showErrorPage(message: String) {
        rvProfile.visibility = View.GONE
        tvContinueWithoutTemplate.visibility = View.GONE
        llNetworkErrorView.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(context, llNetworkErrorView, message, presenter::loadData)
        updateHeight()
    }

    override fun setData(data: ArrayList<ProfileViewModel>) {
        val currentSelectedProfile = arguments?.getParcelable(ARGUMENT_DEFAULT_PROFILE_VIEW_MODEL) as com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel
        for (profileViewModel: ProfileViewModel in data) {
            if (profileViewModel.profileId == currentSelectedProfile.profileId && currentSelectedProfile.isSelected) {
                profileViewModel.isSelected = true
                break
            }
        }
        adapter.setData(data)
        updateHeight()
    }

    override fun onItemSelected(profileViewModel: ProfileViewModel) {
        listener.onProfileChanged(profileViewModel)
    }

    override fun getActivityContext(): Context? {
        return activity
    }

}