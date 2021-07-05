package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_TOPBOT
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREAD
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_UNREPLIED
import com.tokopedia.topchat.chatroom.view.adapter.ChatFilterAdapter
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.ChatFilterTypeFactoryImpl
import com.tokopedia.topchat.chatroom.view.uimodel.ChatFilterUiModel
import com.tokopedia.topchat.chatsetting.view.activity.ChatSettingActivity

class ChatFilterView : LinearLayout {

    private var rvFilter: RecyclerView? = null
    private var settingBtn: ImageView? = null
    private var isSeller = false
    private var rvFilterAdapter: ChatFilterAdapter? = null

    private val sellerFilter = arrayListOf(
            ChatFilterUiModel(R.string.filter_chat_unread, PARAM_FILTER_UNREAD),
            ChatFilterUiModel(R.string.filter_chat_unreplied, PARAM_FILTER_UNREPLIED)
    )
    private val buyerFilter = listOf(
            ChatFilterUiModel(R.string.filter_chat_unread, PARAM_FILTER_UNREAD),
    )

    interface FilterListener {
        fun onFilterChanged(filterType: String)
    }

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context?) {
        initViewInflation(context)
    }

    fun setFilterListener(filterListener: FilterListener) {
        rvFilterAdapter?.filterListener = filterListener
    }

    fun reset() {
        rvFilterAdapter?.reset()
    }

    fun init(isSeller: Boolean) {
        this.isSeller = isSeller
        bindClickNavigation()
        initRecyclerView()
        initFilterData()
    }

    private fun initFilterData() {
        val filter = getFilter()
        rvFilterAdapter?.showFilter(filter)
    }

    private fun getFilter(): List<ChatFilterUiModel> {
        return if (isSeller) {
            sellerFilter
        } else {
            buyerFilter
        }
    }

    private fun initViewInflation(context: Context?) {
        View.inflate(context, R.layout.item_chat_filter, this)?.apply {
            bindView(this)
        }
    }

    private fun bindClickNavigation() {
        settingBtn?.setOnClickListener {
            val intent = ChatSettingActivity.getIntent(context, isSeller)
            startActivity(context, intent, null)
        }
    }

    private fun initRecyclerView() {
        val typeFactory = ChatFilterTypeFactoryImpl()
        rvFilterAdapter = ChatFilterAdapter(typeFactory)
        rvFilter?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false
            )
            adapter = rvFilterAdapter
        }
    }

    private fun bindView(view: View) {
        settingBtn = view.findViewById(R.id.iv_chat_setting)
        rvFilter = view.findViewById(R.id.rv_filter)
    }

    fun onRoleChanged(isSeller: Boolean) {
        this.isSeller = isSeller
        initFilterData()
    }

    fun updateIsWhiteListTopBot(whiteListTopBot: Boolean) {
        if (!whiteListTopBot) return
        val topBotFilter = ChatFilterUiModel(R.string.filter_chat_smart_reply, PARAM_FILTER_TOPBOT)
        if (sellerFilter.size <= 2) {
            sellerFilter.add(topBotFilter)
        }
        updateFilterTopBotSeller(topBotFilter)
    }

    private fun updateFilterTopBotSeller(topBotFilter: ChatFilterUiModel) {
        if (isSeller && rvFilterAdapter?.hasTopBotFilter() == false) {
            rvFilterAdapter?.addTopBotFilter(topBotFilter)
        }
    }

}