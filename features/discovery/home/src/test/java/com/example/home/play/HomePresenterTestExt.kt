package com.example.home.play

import com.tokopedia.home.beranda.presentation.presenter.HomePresenter
import org.spekframework.spek2.dsl.TestBody


fun TestBody.createPresenter(): HomePresenter{
    return HomePresenter()
}