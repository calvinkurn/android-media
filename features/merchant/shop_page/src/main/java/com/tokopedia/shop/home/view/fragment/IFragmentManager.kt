package com.tokopedia.shop.home.view.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created by rizqiaryansa on 2020-02-27.
 */

interface IFragmentManager {
    fun getSupportFragmentManager(): FragmentManager?
    fun getSupportFragment(): Fragment?
}