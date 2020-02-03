package com.example.home

import com.example.home.rules.CoroutinesMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Lukas on 16/11/19
 */
@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class WalletRemoteDataSourceTest{

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Before
    fun init(){

    }

    @Test
    fun `get wallet and tokopoint success`(){

    }

    @Test
    fun `get wallet error and tokopoint success`(){

    }

    @Test
    fun `get wallet success and tokopoint error`(){

    }

    @Test
    fun `has pending cash`(){

    }

    @Test
    fun `on loading get tokopoint`(){

    }

    @Test
    fun `on loading get wallet`(){

    }

    @Test
    fun `still have tokocash`(){

    }
}