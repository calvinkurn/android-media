package com.tokopedia.broadcast.message.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.view.adapter.BroadcastMessageTypeFactory
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.extensions.toISO8601Date
import com.tokopedia.broadcast.message.common.extensions.toStringDayMonth
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData
import com.tokopedia.broadcast.message.view.activity.BroadcastMessageCreateActivity
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageListView
import com.tokopedia.broadcast.message.view.presenter.BroadcastMessageListPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_broadcast_message_list.*
import javax.inject.Inject

class BroadcastMessageListFragment: BaseListFragment<TopChatBlastSeller, BroadcastMessageTypeFactory>(),
        BroadcastMessageListView, BaseEmptyViewHolder.Callback {

    @Inject lateinit var presenter: BroadcastMessageListPresenter

    companion object {
        fun createInstance() = BroadcastMessageListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_broadcast_message_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getMetaData()
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { GraphqlClient.init(it) }
    }

    override fun getAdapterTypeFactory() = BroadcastMessageTypeFactory()

    override fun onItemClicked(t: TopChatBlastSeller?) {

    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(BroadcastMessageComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun loadData(page: Int) {
        presenter.getBlastMessage(page)
    }

    override fun getEmptyDataViewModel() = EmptyModel().apply {
        iconRes = R.drawable.ic_bm_empty_list
        contentRes = R.string.content_empty_string
        buttonTitleRes = R.string.button_text_empty
        callback = this@BroadcastMessageListFragment
    }

    override fun getSwipeRefreshLayout(view: View?) = swipe_refresh

    override fun getEndlessLayoutManagerListener() = EndlessLayoutManagerListener {
        getRecyclerView(view).layoutManager
    }

    override fun onEmptyContentItemTextClicked() {

    }

    override fun onEmptyButtonClicked() {
        gotoCreateBroadcast()
    }

    private fun gotoCreateBroadcast() {
        context?.let { startActivity(BroadcastMessageCreateActivity.createIntent(it)) }
    }

    override fun onSuccessGetMetaData(metaData: TopChatBlastSellerMetaData?) {
        metaData?.let {
            val spannableBuilder = SpannableStringBuilder()
            spannableBuilder.append("${getString(R.string.template_bm_quota)} ")
            val boldString = SpannableString(it.quota.toString())
            boldString.setSpan(StyleSpan(Typeface.BOLD), 0, boldString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableBuilder.append(boldString)
            bm_quota.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
            bm_quota_refill.text = getString(R.string.template_bm_quota_refill, it.expireAt.toISO8601Date().toStringDayMonth())
        }
    }

    override fun onErrorGetMetaData(throwable: Throwable) {

    }

    override fun onSuccessGetBlastMessage(response: TopChatBlastSeller.Response?) {
        super.renderList(response?.list ?: listOf(), response?.hasNext == true)
    }

    override fun onErrorGetBlastMessage(throwable: Throwable?) {

    }
}