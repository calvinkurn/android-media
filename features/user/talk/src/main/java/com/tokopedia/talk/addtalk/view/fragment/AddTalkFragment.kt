package com.tokopedia.talk.addtalk.view.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.design.component.ToasterError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.R
import com.tokopedia.talk.addtalk.di.DaggerAddTalkComponent
import com.tokopedia.talk.addtalk.presenter.AddTalkPresenter
import com.tokopedia.talk.addtalk.view.activity.AddTalkActivity
import com.tokopedia.talk.addtalk.view.adapter.QuickReplyAdapter
import com.tokopedia.talk.addtalk.view.adapter.QuickReplyTalkViewHolder
import com.tokopedia.talk.addtalk.view.adapter.QuickReplyTypeFactoryImpl
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk.common.analytics.TalkAnalytics
import com.tokopedia.talk.common.di.TalkComponent
import kotlinx.android.synthetic.main.layout_talk_add.*
import javax.inject.Inject

/**
 * @author by Steven
 */

class AddTalkFragment : BaseDaggerFragment(),
        AddTalkContract.View, QuickReplyTalkViewHolder.PasteTemplateListener {

    @Inject
    lateinit var presenter: AddTalkPresenter

    @Inject
    lateinit var analytics: TalkAnalytics

    lateinit var adapter: QuickReplyAdapter

    var productId: String = ""

    override fun initInjector() {
        val addTalkComponent = DaggerAddTalkComponent.builder()
                .talkComponent(getComponent(TalkComponent::class.java))
                .build()
        addTalkComponent.inject(this)
        presenter.attachView(this)
    }


    override fun getContext(): Context? {
        return activity
    }

    override fun getScreenName(): String {
        return TalkAnalytics.SCREEN_NAME_ADD_TALK
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            analytics.sendScreen(this, screenName)
        }
    }

    companion object {

        fun newInstance(extras: Bundle): AddTalkFragment {
            val fragment = AddTalkFragment()
            fragment.productId = extras.getString(AddTalkActivity.EXTRA_PRODUCT_ID)
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_talk_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
    }

    private fun setUpView(view: View) {
        val adapterTypeFactory = QuickReplyTypeFactoryImpl(this)
        adapter = QuickReplyAdapter(adapterTypeFactory)
        val quickReplyItemDecoration = QuickReplyItemDecoration(activity!!
                .resources.getDimension(R.dimen.dp_16).toInt())
        list_template.addItemDecoration(quickReplyItemDecoration)
        send_new_talk.setOnClickListener {
            send_progress.visibility = View.VISIBLE
            presenter.send(productId, message_talk.text.toString())
        }
    }

    class QuickReplyItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State?) {

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = space
            } else if (parent.getChildAdapterPosition(view) == parent.adapter.itemCount - 1) {
                outRect.right = space
            }
            outRect.top = view.context.resources.getDimension(R.dimen.dp_8).toInt()
        }
    }

    override fun onTemplateClicked() {

    }

    override fun onErrorCreateTalk(throwable: Throwable?) {
        send_progress.visibility = View.GONE

        if (throwable is MessageErrorException) {
            ToasterError.make(view, throwable.message, Snackbar.LENGTH_LONG).show()
        } else {
            ToasterError.make(view, ErrorHandler.getErrorMessage(context, throwable)
                    ?: "", Snackbar.LENGTH_LONG).show()
        }

    }

    override fun onSuccessCreateTalk(productId: String) {
        send_progress.visibility = View.GONE
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onDestroyView() {
        KeyboardHandler.hideSoftKeyboard(activity)
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
