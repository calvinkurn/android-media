package com.tokopedia.talk.addtalk.view.fragment

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.component.Dialog
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.R
import com.tokopedia.talk.addtalk.di.DaggerAddTalkComponent
import com.tokopedia.talk.addtalk.presenter.AddTalkPresenter
import com.tokopedia.talk.addtalk.view.adapter.QuickReplyAdapter
import com.tokopedia.talk.addtalk.view.adapter.QuickReplyTypeFactoryImpl
import com.tokopedia.talk.addtalk.view.listener.AddTalkContract
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkAdapter
import kotlinx.android.synthetic.main.layout_talk_add.*
import kotlinx.android.synthetic.main.product_talk.*
import javax.inject.Inject

/**
 * @author by Steven
 */

class AddTalkFragment : BaseDaggerFragment(),
                 AddTalkContract.View   {

    @Inject
    lateinit var presenter: AddTalkPresenter

    lateinit var adapter: QuickReplyAdapter

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun newInstance(extras: Bundle): AddTalkFragment {
            val fragment = AddTalkFragment()
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
}
