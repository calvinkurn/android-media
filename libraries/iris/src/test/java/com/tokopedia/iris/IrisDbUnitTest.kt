package com.tokopedia.iris

import android.content.Context
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

/**
 * Created by meta on 02/01/19.
 */
@RunWith(MockitoJUnitRunner::class)
class IrisDbUnitTest {

    @Mock private lateinit var context: Context

    private lateinit var trackingDao: TrackingDao

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        trackingDao = IrisDb.getInstance(context).trackingDao()
    }

    fun getEventOldestByLimit() {
    }

    fun deleteEventByList() {

    }

    fun insertEvent() {

    }
}