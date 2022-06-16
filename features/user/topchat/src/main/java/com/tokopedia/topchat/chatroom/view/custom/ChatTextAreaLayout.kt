package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.srw.SrwQuestionViewHolder
import com.tokopedia.unifyprinciples.Typography

class ChatTextAreaLayout: ConstraintLayout {

    /**
     * Tab SRW
     */
    private var tabSRW: View? = null
    private var textTabSRW: Typography? = null
    private var srwLayout : SrwFrameLayout? = null

    /**
     * Tab Tulis Pesan
     */
    private var tabReplyBox: View? = null
    private var textTabReplyBox: Typography? = null
    private var replyBoxLayout : ComposeMessageAreaConstraintLayout? = null

    /**
     * List of tab-layout
     */
    private var tabList = hashMapOf<View?, Pair<View?, View?>>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initViewLayout()
        initViewBind()
        initListener()
        initSrw()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBind() {
        tabSRW = findViewById(R.id.srw_tab_bg)
        textTabSRW = findViewById(R.id.tv_srw_tab)
        srwLayout = findViewById(R.id.rv_srw)

        tabReplyBox = findViewById(R.id.textarea_tab_bg)
        textTabReplyBox = findViewById(R.id.tv_textarea_tab)
        replyBoxLayout = findViewById(R.id.reply_box)

        tabList.apply {
            this[textTabSRW] = (Pair(tabSRW, srwLayout))
            this[textTabReplyBox] = (Pair(tabReplyBox, replyBoxLayout))
        }
    }

    private fun initListener() {
        tabSRW?.setOnClickListener {
            updateTab(it)
        }
        tabReplyBox?.setOnClickListener {
            updateTab(it)
        }
    }

    private fun initSrw() {
        srwLayout?.initialize(getSrwQuestionListener(), getSrwFrameLayoutListener())
        /**
         * Remove this testing
         */
        val srwQuestions = ChatSmartReplyQuestionResponse().also {
            it.chatSmartReplyQuestion.hasQuestion = true
            it.chatSmartReplyQuestion.title = "SRW Question Testing"
            it.chatSmartReplyQuestion.questions = listOf(
                QuestionUiModel(content = "SRW Question 1?"),
                QuestionUiModel(content = "SRW Question 2?"),
                QuestionUiModel(content = "SRW Question 3?"),
                QuestionUiModel(content = "SRW Question 4?")
            )
        }
        srwLayout?.updateSrwList(srwQuestions)
        srwLayout?.setSrwTitleVisibility(false)
        srwLayout?.setContentMargin()
    }

    private fun getSrwQuestionListener(): SrwQuestionViewHolder.Listener {
        return object : SrwQuestionViewHolder.Listener {
            override fun onClickSrwQuestion(question: QuestionUiModel) {
                TODO("Not yet implemented")
            }

            override fun trackClickSrwQuestion(question: QuestionUiModel) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun getSrwFrameLayoutListener(): SrwFrameLayout.Listener {
        return object : SrwFrameLayout.Listener {
            override fun trackViewSrw() {
                TODO("Not yet implemented")
            }

            override fun onExpandStateChanged(isExpanded: Boolean) {
                TODO("Not yet implemented")
            }

            override fun shouldShowOnBoarding(): Boolean {
                TODO("Not yet implemented")
            }
        }
    }

    private fun updateTab(pickedView: View) {
        changeTabState(pickedView, TAB_ACTIVE)
        showTabContent(pickedView)
        changeToInactive(pickedView)
    }

    private fun changeTabState(pickedView: View, resource: Int) {
        pickedView.setBackgroundResource(resource)
    }

    private fun showTabContent(pickedView: View) {
        tabList.forEach {
            val text = it.key
            val tab = it.value.first
            val layout = it.value.second
            if (tab == pickedView) {
                layout?.show()
                tab.bringToFront()
                text?.bringToFront()
                return@forEach
            }
        }
    }

    private fun changeToInactive(pickedView: View) {
        tabList.forEach {
            val tab = it.value.first
            val layout = it.value.second
            if (tab != pickedView) {
                tab?.let { tabView ->
                    changeTabState(tabView, TAB_INACTIVE)
                    layout?.hide()
                }
            }
        }
    }

    companion object {
        private val LAYOUT = R.layout.layout_text_area_tab
        private val TAB_ACTIVE = R.drawable.bg_text_area_tab_active
        private val TAB_INACTIVE = R.drawable.bg_text_area_tab_inactive
    }
}