package com.tokopedia.talk.feature.reading.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel

class TalkReadingFragment : BaseListFragment<TalkReadingUiModel, TalkReadingAdapterTypeFactory>() {

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        getComponent(TalkReadingComponent::class.java).inject(this)
    }

    override fun onItemClicked(t: TalkReadingUiModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}