package com.tokopedia.talk.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.talk_old.R
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import javax.inject.Inject

class TalkReadingFragment : BaseListFragment<TalkReadingUiModel, TalkReadingAdapterTypeFactory>(), HasComponent<TalkReadingComponent> {

    companion object {

        const val PRODUCT_ID = "product_id"

        @JvmStatic
        fun createNewInstance(productId: Int): TalkReadingFragment =
            TalkReadingFragment().apply {
                arguments?.putInt(PRODUCT_ID, productId)
            }
    }

    @Inject
    lateinit var viewModel: TalkReadingViewModel

    private var productId: Int = 0

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        return TalkReadingAdapterTypeFactory()
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkReadingRecyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getProductId()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showPageLoading()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun onItemClicked(t: TalkReadingUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {
        // get data
    }

    override fun getComponent(): TalkReadingComponent {
        return DaggerTalkReadingComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }

    private fun getProductId() {
        arguments?.let {
            productId = it.getInt(PRODUCT_ID)
        }
    }

    private fun showPageLoading() {
        pageLoading.visibility = View.VISIBLE
    }

    private fun showPageError() {
        pageError.visibility = View.VISIBLE
    }
}