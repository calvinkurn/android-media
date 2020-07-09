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
import com.tokopedia.interestpick.view.listener.InterestPickContract
import kotlinx.android.synthetic.main.fragment_interest_pick.*
import javax.inject.Inject

/**
 * @author by milhamj on 03/09/18.
 */

class InterestPickFragment : BaseDaggerFragment(), InterestPickContract.View, InterestPickAdapter.InterestPickItemListener {

    companion object {
        fun createInstance() = InterestPickFragment()
    }

    @Inject
    lateinit var presenter: InterestPickContract.Presenter
    lateinit var adapter: InterestPickAdapter
    var selectedCount = 0
    var isSaved = false

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

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
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

    override fun onSuccessGetInterest(interestList: ArrayList<InterestPickDataViewModel>,
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
        NetworkErrorHelper.showEmptyState(context, view, message, {
            presenter.fetchData()
        })
    }

    override fun onSuccessUpdateInterest() {
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
                RouteManager.route(context, ApplinkConst.HOME_ACCOUNT);
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
                subtitle.indexOf(subtitleEnd, ignoreCase=true),
                subtitle.indexOf(subtitleEnd, ignoreCase=true) + subtitleEnd.length,
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
        isSaved = true
    }

    override fun onErrorUpdateInterest(message: String) {
        NetworkErrorHelper.showRedSnackbar(view, message)
    }

    override fun onBackPressed() {
        if (!isSaved) {
            presenter.onBackPressed()
        }
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
        adapter = InterestPickAdapter(this, SOURCE_ACCOUNTS)
        interestList.adapter = adapter
        saveInterest.setOnClickListener {
            presenter.updateInterest(adapter.getSelectedItemIdList())
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
