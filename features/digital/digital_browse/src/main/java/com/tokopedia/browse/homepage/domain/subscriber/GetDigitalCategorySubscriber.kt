package com.tokopedia.browse.homepage.domain.subscriber

import android.content.Context

import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceViewModel

import rx.Subscriber

/**
 * @author by furqan on 07/09/18.
 */

class GetDigitalCategorySubscriber(private val digitalCategoryActionListener: DigitalCategoryActionListener, private val context: Context) : Subscriber<DigitalBrowseServiceViewModel>() {

    override fun onCompleted() {

    }

    override fun onError(throwable: Throwable) {
        digitalCategoryActionListener.onErrorGetDigitalCategory(throwable)
    }

    override fun onNext(viewModel: DigitalBrowseServiceViewModel) {
        digitalCategoryActionListener.onSuccessGetDigitalCategory(viewModel)
    }

    interface DigitalCategoryActionListener {

        fun onErrorGetDigitalCategory(throwable: Throwable)

        fun onSuccessGetDigitalCategory(digitalBrowseServiceViewModel: DigitalBrowseServiceViewModel)

    }
}
