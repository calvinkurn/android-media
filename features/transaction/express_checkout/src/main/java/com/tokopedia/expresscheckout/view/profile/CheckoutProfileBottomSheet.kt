package com.tokopedia.expresscheckout.view.profile

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ToasterError
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.profile.adapter.CheckoutProfileAdapter
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfileBottomSheet : BottomSheets(), CheckoutProfileContract.View, CheckoutProfileActionListener {

    private lateinit var adapter: CheckoutProfileAdapter
    private lateinit var presenter: CheckoutProfileContract.Presenter
    private lateinit var listener: CheckoutProfileFragmentListener
    private lateinit var tvContinueWithoutTemplate: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var rvProfile: RecyclerView

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

    override fun initView(view: View?) {
        tvContinueWithoutTemplate = view?.findViewById<View>(R.id.tv_continue_without_template) as TextView
        pbLoading = view.findViewById<View>(R.id.pb_loading) as ProgressBar
        rvProfile = view.findViewById<View>(R.id.rv_profile) as RecyclerView

        presenter = CheckoutProfilePresenter()
        presenter.attachView(this)
        presenter.loadData()

        adapter = CheckoutProfileAdapter(ArrayList(), this)

        tvContinueWithoutTemplate.setOnClickListener {
            for (profileViewModel: ProfileViewModel in adapter.data) {
                profileViewModel.isSelected = false
            }
            listener.onContinueWithoutProfile()
        }
        rvProfile.adapter = adapter
        rvProfile.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
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