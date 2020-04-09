package com.tokopedia.talk.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk_old.R

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reading, container)
    }
}