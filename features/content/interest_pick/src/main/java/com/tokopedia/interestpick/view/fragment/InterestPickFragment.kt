package com.tokopedia.interestpick.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.interest_pick_common.view.adapter.InterestPickAdapter
import com.tokopedia.interest_pick_common.view.adapter.InterestPickAdapter.Companion.SOURCE_ACCOUNTS
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interestpick.R
import com.tokopedia.interestpick.di.DaggerInterestPickComponent
import com.tokopedia.interestpick.view.subscriber.InterestPickViewState
import com.tokopedia.interestpick.view.viewmodel.InterestPickViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.fragment_interest_pick.*
import javax.inject.Inject

/**
 * @author by milhamj on 03/09/18.
 */

class InterestPickFragment : BaseDaggerFragment(), InterestPickAdapter.InterestPickItemListener {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var adapter: InterestPickAdapter
    private lateinit var interestPickViewModel: InterestPickViewModel

    private var selectedCount = 0

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
        setUpObservers()
        interestPickViewModel.onViewCreated()
    }

    private fun setUpObservers() {
        interestPickViewModel.getInterestPickLiveData().observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is InterestPickViewState.GetInterestSuccess -> {
                    onSuccessGetInterest(it.interestList, it.headerModel)
                }
                is InterestPickViewState.UpdateInterestSuccess -> {
                    onSuccessUpdateInterest()
                }
                is InterestPickViewState.Error -> {
                    if (it.onUpdate) {
                        onErrorUpdateInterest(it.error)
                    } else {
                        onErrorGetInterest(it.error)
                    }

                }
                is InterestPickViewState.LoadingState -> {
                    showLoading()
                }
                is InterestPickViewState.ProgressState -> {
                    showProgress()
                }
            }
        })
    }

    private fun showLoading() {
        mainView.hide()
        loadingView.show()
    }

    private fun showProgress() {
        progressBar.show()
    }

    private fun hideLoading() {
        mainView.show()
        loadingView.hide()
        progressBar.hide()
    }

    private fun onSuccessGetInterest(interestList: ArrayList<InterestPickDataViewModel>,
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

    private fun onErrorGetInterest(message: String) {
        NetworkErrorHelper.showEmptyState(context, view, message) {
            interestPickViewModel.onRetry()
        }
    }

    private fun onSuccessUpdateInterest() {
        val selectedList = ArrayList<InterestPickDataViewModel>()
        for (item in adapter.getList()) {
            if (item.isSelected) {
                item.isClickable = false
                selectedList.add(item)
            }
        }
        adapter.setList(selectedList)
        titleTextView.text = getString(R.string.interest_enjoy)

        val goToAccountHome = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, ApplinkConst.HOME_ACCOUNT)
                activity?.finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MethodChecker.getColor(context, com.tokopedia.abstraction.R.color.tkpd_main_green)
            }
        }
        val subtitle = getString(R.string.interest_change)
        val subtitleEnd = getString(R.string.interest_account)
        val subtitleSpan = SpannableString(subtitle)
        subtitleSpan.setSpan(
                goToAccountHome,
                subtitle.indexOf(subtitleEnd, ignoreCase = true),
                subtitle.indexOf(subtitleEnd, ignoreCase = true) + subtitleEnd.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        subtitleTextView.text = subtitleSpan
        subtitleTextView.movementMethod = LinkMovementMethod.getInstance()
        subtitleTextView.visibility = View.VISIBLE
        saveInterest.text = getString(R.string.interest_check_feed_now)
        saveInterest.setOnClickListener {
            RouteManager.route(context, ApplinkConst.FEED)
            activity?.finish()
        }
    }

    private fun onErrorUpdateInterest(message: String) {
        NetworkErrorHelper.showRedSnackbar(view, message)
    }

    fun onBackPressed() {
        interestPickViewModel.onBackPressed()
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
        activity?.let {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            interestPickViewModel = viewModelProvider.get(InterestPickViewModel::class.java)
        }
        adapter = InterestPickAdapter(this, SOURCE_ACCOUNTS)
        interestList.adapter = adapter
        saveInterest.setOnClickListener {
            interestPickViewModel.updateInterest(adapter.getSelectedItemIdList())
        }
    }

    override fun onInterestPickItemClicked(item: InterestPickDataViewModel) {
        selectedCount = adapter.getSelectedItemIdList().size
        updateSaveButtonState()
    }

    override fun onLihatSemuaItemClicked(selectedItemList: List<InterestPickDataViewModel>) {
    }

    override fun onCheckRecommendedProfileButtonClicked(selectedItemList: List<InterestPickDataViewModel>) {
    }
}
