package com.tokopedia.play.generator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayViewerIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    
}