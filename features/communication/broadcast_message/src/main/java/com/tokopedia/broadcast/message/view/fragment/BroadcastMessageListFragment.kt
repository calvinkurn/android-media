package com.tokopedia.broadcast.message.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener
import com.tokopedia.broadcast.message.data.model.TopChatBlastSeller
import com.tokopedia.broadcast.message.view.adapter.BroadcastMessageTypeFactory
import com.tokopedia.broadcast.message.R
import com.tokopedia.broadcast.message.common.BroadcastMessageRouter
import com.tokopedia.broadcast.message.common.constant.BroadcastMessageConstant
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.extensions.toStringDayMonth
import com.tokopedia.broadcast.message.common.data.model.TopChatBlastSellerMetaData
import com.tokopedia.broadcast.message.common.extensions.dateToShow
import com.tokopedia.broadcast.message.view.activity.BroadcastMessageCreateActivity
import com.tokopedia.broadcast.message.view.listener.BroadcastMessageListView
import com.tokopedia.broadcast.message.view.presenter.BroadcastMessageListPresenter
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.track.TrackApp
import kotlinx.android.synthetic.main.fragment_broadcast_message_list.*
import javax.inject.Inject

class BroadcastMessageListFragment: BaseListFragment<TopChatBlastSeller, BroadcastMessageTypeFactory>(),
        BroadcastMessageListView, BaseEmptyViewHolder.Callback {

    private var validToCreate = false
    private var hasMessage = false

    @Inject lateinit var presenter: BroadcastMessageListPresenter

    companion object {
        private const val REQUEST_ADD_BROADCAST_MESSAGE = 0x15
        fun createInstance() = BroadcastMessageListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_broadcast_message_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val list = getRecyclerView(view)
            list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                .apply { setDrawable(ContextCompat.getDrawable(it, R.drawable.broadcast_message_card_divider)!!)})
        }
        adapter.errorNetworkModel = ErrorNetworkModel().apply { iconDrawableRes = R.drawable.ic_error_network }

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

    override fun onItemClicked(t: TopChatBlastSeller?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(BroadcastMessageComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun onSwipeRefresh() {
        presenter.getMetaData()
        super.onSwipeRefresh()
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
        TrackApp.getInstance()?.gtm?.sendGeneralEvent(BroadcastMessageConstant.VALUE_GTM_EVENT_NAME_INBOX,
                        BroadcastMessageConstant.VALUE_GTM_EVENT_CATEGORY,
                        BroadcastMessageConstant.VALUE_GTM_EVENT_ACTION_CREATE_CLICK,"")
        context?.let {
            startActivityForResult(BroadcastMessageCreateActivity.createIntent(it),
                REQUEST_ADD_BROADCAST_MESSAGE) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ADD_BROADCAST_MESSAGE && data != null){
            val needRefresh = data.getBooleanExtra(BroadcastMessageConstant.PARAM_NEED_REFRESH, false)
            if (needRefresh){
                presenter.getMetaData()
                loadInitialData()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_bm_create, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val item = menu?.findItem(R.id.add_bm)
        item?.setVisible(hasMessage)
        item?.isEnabled = validToCreate

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.add_bm){
            if (validToCreate)
                gotoCreateBroadcast()
            return validToCreate
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSuccessGetMetaData(metaData: TopChatBlastSellerMetaData?) {
        metaData?.let {
            val spannableBuilder = SpannableStringBuilder()
            spannableBuilder.append("${getString(R.string.template_bm_quota)} ")
            val boldString = SpannableString(it.quota.toString())
            boldString.setSpan(StyleSpan(Typeface.BOLD), 0, boldString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableBuilder.append(boldString)
            bm_quota.setText(spannableBuilder, TextView.BufferType.SPANNABLE)
            bm_quota_refill.text = getString(
                    if (it.quota > 0) R.string.template_bm_quota_valid_usage else R.string.template_bm_quota_refill,
                    it.dateToShow?.toStringDayMonth())
            validToCreate = it.hasActiveQuota
        }
        activity?.invalidateOptionsMenu()
    }

    override fun onErrorGetMetaData(throwable: Throwable) {}

    override fun onSuccessGetBlastMessage(response: TopChatBlastSeller.BlastSellerList?) {
        super.renderList(response?.list ?: listOf(), response?.hasNext == true)
        hasMessage = adapter.dataSize > 0
        activity?.invalidateOptionsMenu()
    }

    override fun onErrorGetBlastMessage(throwable: Throwable?) {
        super.showGetListError(throwable)
    }
}