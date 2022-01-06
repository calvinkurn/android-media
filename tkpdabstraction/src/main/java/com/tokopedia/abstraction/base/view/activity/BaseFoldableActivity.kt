package com.tokopedia.abstraction.base.view.activity

import android.os.Bundle
import androidx.window.WindowInfoRepo
import androidx.window.windowInfoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


abstract class BaseFoldableActivity : BaseActivity() {

    private lateinit var windowInfoRepo: WindowInfoRepo

    private var layoutUpdatesJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowInfoRepo = windowInfoRepository()
    }

    override fun onStart() {
        super.onStart()
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            windowInfoRepo.windowLayoutInfo()
                .collect { newLayoutInfo ->
                    newLayoutInfo
//                    changeLayout(newLayoutInfo)
                }
        }
    }

    @Override
    override fun onStop() {
        super.onStop()
        layoutUpdatesJob?.cancel()
    }

}