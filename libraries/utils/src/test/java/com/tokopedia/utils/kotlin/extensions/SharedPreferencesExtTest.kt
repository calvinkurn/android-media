package com.tokopedia.utils.kotlin.extensions

import android.content.SharedPreferences.Editor
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class SharedPreferencesExtTest {

    @RelaxedMockK
    lateinit var editor: Editor

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `when backgroundCommit is executed expect editor to commit`() {
        editor.backgroundCommit()
        coVerify {
            editor.commit()
        }
    }
}
