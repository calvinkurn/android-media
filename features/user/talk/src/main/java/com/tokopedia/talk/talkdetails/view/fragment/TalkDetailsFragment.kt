package com.tokopedia.talk.talkdetails.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.talkdetails.di.DaggerTalkDetailsComponent
import com.tokopedia.talk.talkdetails.view.activity.TalkDetailsActivity
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactoryImpl
import com.tokopedia.talk.talkdetails.view.contract.TalkDetailsContract
import com.tokopedia.talk.talkdetails.view.presenter.TalkDetailsPresenter
import com.tokopedia.talk.R
import com.tokopedia.talk.reporttalk.view.activity.ReportTalkActivity
import com.tokopedia.talk.talkdetails.view.adapter.TalkDetailsAdapter
import kotlinx.android.synthetic.main.fragment_talk_comments.*
import javax.inject.Inject

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsFragment : BaseListFragment<Visitable<*>, TalkDetailsTypeFactoryImpl>(),
                            TalkDetailsContract.View {
    @Inject
    lateinit var presenter: TalkDetailsPresenter

    lateinit var adapter: TalkDetailsAdapter

    override fun getAdapterTypeFactory(): TalkDetailsTypeFactoryImpl {
        return TalkDetailsTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        presenter.loadTalkDetails(arguments?.getString(TalkDetailsActivity.THREAD_TALK_ID)?:"")
    }

    override fun getScreenName(): String {
        return "Talk Details"
    }

    override fun initInjector() {
        val talkDetailsComponent = DaggerTalkDetailsComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        talkDetailsComponent.inject(this)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_comments,container)
    }

    //TalkDetailsContract.View
    override fun onError(throwable: Throwable) {
        showGetListError(throwable)
    }

    override fun onSuccessLoadTalkDetails(data: List<Visitable<*>>) {
        renderList(data)
        disableLoadMore()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, TalkDetailsTypeFactoryImpl> {
        adapter = TalkDetailsAdapter(adapterTypeFactory)
        return adapter
    }

    override fun onSuccessDeleteTalkComment(id: String) {
        adapter.removeCommentWithId(id)
    }

    override fun goToReportTalkPage(id: String) {
        val intent = ReportTalkActivity.createIntent(context!!)
        startActivityForResult(intent,GO_TO_REPORT_TALK_REQ_CODE)
    }

    override fun onSuccessSendTalkComment(item: Visitable<*>) {
        adapter.addItem(item)
    }

    companion object {
        const val GO_TO_REPORT_TALK_REQ_CODE = 101
    }
}