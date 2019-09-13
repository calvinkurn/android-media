package com.tokopedia.feedplus.profilerecommendation.view.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.adapter.FollowRecommendationAdapter
import com.tokopedia.feedplus.profilerecommendation.view.contract.FollowRecommendationContract
import com.tokopedia.feedplus.profilerecommendation.view.presenter.FollowRecommendationPresenter
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationInfoViewModel
import com.tokopedia.feedplus.profilerecommendation.di.DaggerFollowRecommendationComponent
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationFragment : BaseDaggerFragment(), FollowRecommendationContract.View, FollowRecommendationAdapter.ActionListener {

    companion object {

        fun newInstance(): FollowRecommendationFragment {
            return FollowRecommendationFragment()
        }
    }

    @Inject
    lateinit var presenter: FollowRecommendationPresenter

    var cursor = ""

    private lateinit var followRecommendationAdapter: FollowRecommendationAdapter

    private lateinit var rvFollowRecom: RecyclerView
    private lateinit var btnAction: UnifyButton
    private lateinit var tvInfo: TextView

    override fun getScreenName(): String {
        return "Follow Recommendation"
    }

    override fun initInjector() {
        val component = DaggerFollowRecommendationComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                .build()

        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attachView(this)
        return inflater.inflate(R.layout.fragment_follow_recommendation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        presenter.getFollowRecommendationList(listOf(1), cursor)
    }

    private fun initView(view: View) {
        view.run {
            rvFollowRecom = findViewById(R.id.rv_follow_recom)
            btnAction = findViewById(R.id.btn_action)
            tvInfo = findViewById(R.id.tv_info)
        }
    }

    private fun setupView(view: View) {
        followRecommendationAdapter = FollowRecommendationAdapter(emptyList(), this)
        rvFollowRecom.apply {
            adapter = followRecommendationAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onGetFollowRecommendationList(recomList: List<FollowRecommendationCardViewModel>, cursor: String) {
        followRecommendationAdapter.apply {
            addItems(recomList)
        }
    }

    override fun onGetFollowRecommendationInfo(infoViewModel: FollowRecommendationInfoViewModel) {
        btnAction.text = infoViewModel.buttonCTA
        tvInfo.text = infoViewModel.instructionText
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onFollowButtonClicked(authorId: String) {

    }
}