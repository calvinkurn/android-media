package com.tokopedia.talk_old.addtalk.view.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.addtalk.di.DaggerAddTalkComponent
import com.tokopedia.talk_old.addtalk.presenter.AddTalkPresenter
import com.tokopedia.talk_old.addtalk.view.activity.AddTalkActivity
import com.tokopedia.talk_old.addtalk.view.adapter.QuickReplyAdapter
import com.tokopedia.talk_old.addtalk.view.adapter.QuickReplyTalkViewHolder
import com.tokopedia.talk_old.addtalk.view.adapter.QuickReplyTypeFactoryImpl
import com.tokopedia.talk_old.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk_old.common.analytics.TalkAnalytics
import com.tokopedia.talk_old.common.di.TalkComponent
import com.tokopedia.unifycomponents.Toaster
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
    var source: String = ""

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
            fragment.productId = extras.getString(AddTalkActivity.EXTRA_PRODUCT_ID) ?: ""
            fragment.source = extras.getString(TalkConstants.PARAM_SOURCE) ?: ""
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
                .resources.getDimension(com.tokopedia.design.R.dimen.dp_16).toInt())
        list_template.addItemDecoration(quickReplyItemDecoration)
        send_new_talk.setOnClickListener {
            send_progress.visibility = View.VISIBLE
            analytics.trackClickSendNewTalk(productId)
            presenter.send(productId, message_talk.text.toString())
        }
        message_talk.requestFocus()
    }

    class QuickReplyItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                    state: RecyclerView.State) {

            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = space
            } else if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount?.minus(1) ?: -1) {
                outRect.right = space
            }
            outRect.top = view.context.resources.getDimension(com.tokopedia.design.R.dimen.dp_8).toInt()
        }
    }

    override fun onTemplateClicked() {

    }

    override fun onErrorCreateTalk(throwable: Throwable?) {
        send_progress.visibility = View.GONE

        if (throwable is MessageErrorException) {
            view?.let { throwable.message?.let { message -> Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR) } }
        } else {
            view?.let { ErrorHandler.getErrorMessage(context, throwable)?.let { message -> Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR) } }
        }

    }

    override fun onSuccessCreateTalk(productId: String) {
        send_progress.visibility = View.GONE
        goToReading(productId)
    }

    private fun goToReading(productId: String) {
        if(source == TalkConstants.READING_SOURCE) {
            activity?.run {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            context?.let {
                startActivity(RouteManager.getIntent(it, Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.PRODUCT_TALK, productId)).toString()))
            }
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
