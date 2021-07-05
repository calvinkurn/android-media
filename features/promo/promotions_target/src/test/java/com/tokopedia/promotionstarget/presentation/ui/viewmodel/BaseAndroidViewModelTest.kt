package com.tokopedia.promotionstarget.presentation.ui.viewmodel

import android.app.Application
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.job
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.spekframework.spek2.Spek
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class BaseAndroidViewModelTest : Spek({
    lateinit var baseAndroidViewModel: BaseAndroidViewModel

    test("getter"){
        val baseDispatcher = TestCoroutineDispatcher()
        val app: Application = mockk()
        baseAndroidViewModel = object :BaseAndroidViewModel(baseDispatcher,app){}

        assertEquals(baseAndroidViewModel.coroutineContext[ContinuationInterceptor.Key] == baseDispatcher, true)
        assertEquals(baseAndroidViewModel.coroutineContext.job == baseAndroidViewModel.masterJob, true)
        baseDispatcher.cleanupTestCoroutines()
    }
})