package com.tokopedia.interestpick.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.di.DaggerInterestPickComponent
import com.tokopedia.interestpick.view.adapter.InterestPickAdapter
import com.tokopedia.interestpick.view.listener.InterestPickContract
import com.tokopedia.interestpick.view.viewmodel.InterestPickItemViewModel
import kotlinx.android.synthetic.main.fragment_interest_pick.*
import javax.inject.Inject

/**
 * @author by milhamj on 03/09/18.
 */

class InterestPickFragment : BaseDaggerFragment(), InterestPickContract.View {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    @Inject
    lateinit var presenter: InterestPickContract.Presenter
    lateinit var adapter: InterestPickAdapter
    var selectedCount = 0

    override fun getScreenName() = null

    override fun initInjector() {
        activity?.let {
            (it.applicationContext as BaseMainApplication).baseAppComponent
        }.let {
            DaggerInterestPickComponent.builder()
                    .baseAppComponent(it)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_interest_pick, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        presenter.attachView(this)
        presenter.fetchData()
    }

    override fun showLoading() {
        mainView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mainView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        progressBar.visibility = View.GONE
    }

    override fun onSuccessGetInterest(interestList: ArrayList<InterestPickItemViewModel>,
                                      title: String) {
        for (item in interestList) {
            if (item.isSelected) {
                selectedCount++
            }
        }
        updateSaveButtonState()

        adapter.setList(interestList)
        titleTextView.text = title
    }

    override fun onErrorGetInterest(message: String) {
        NetworkErrorHelper.showEmptyState(context, view, {
            presenter.fetchData()
        })
    }

    override fun onSuccessUpdateInterest() {
        val selectedList = ArrayList<InterestPickItemViewModel>()
        for (item in adapter.getList()) {
            if (item.isSelected) {
                selectedList.add(item)
            }
        }
        adapter.setList(selectedList)
        titleTextView.text = getString(R.string.interest_enjoy)
        subtitleTextView.visibility = View.VISIBLE
        saveInterest.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onErrorUpdateInterest(message: String) {
        NetworkErrorHelper.showRedSnackbar(view, message)
    }

    override fun onItemSelected(isSelected: Boolean) {
        if (isSelected) {
            selectedCount++
        } else {
            selectedCount--
        }

        updateSaveButtonState()
    }

    private fun updateSaveButtonState() {
        if (selectedCount >= 1) {
            saveInterest.text = getString(R.string.interest_save)
            saveInterest.isEnabled = true
        } else {
            saveInterest.text = getString(R.string.interest_select_one)
            saveInterest.isEnabled = false
        }
    }

    private fun initView() {
        adapter = InterestPickAdapter(this)
        interestList.adapter = adapter
        saveInterest.setOnClickListener {
            updateInterest()
        }
    }

    private fun updateInterest() {
        val selectedIds = ArrayList<Int>()
        for (item in adapter.getList()) {
            adapter.setItemUnClickable(item)
            if (item.isSelected) {
                selectedIds.add(item.categoryId)
            }
        }
        presenter.updateInterest(selectedIds.toTypedArray())
    }
}