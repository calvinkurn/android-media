package com.tkpd.remoteresourcerequest.database

import android.content.Context
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ResourceDBTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getResourceEntryDao() {

        val context = mockk<Context>(relaxed = true)

        val instance = ResourceDB.getDatabase(context)
        val anotherInstance = ResourceDB.getDatabase(context)

        assertEquals(instance, anotherInstance)
    }
}