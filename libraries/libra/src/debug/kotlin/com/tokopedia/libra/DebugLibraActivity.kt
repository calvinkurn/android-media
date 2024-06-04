@file:SuppressLint("SetTextI18n")

package com.tokopedia.libra

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.libra.di.DaggerDebugLibraComponent
import com.tokopedia.libra.di.DebugLibraModule
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DebugLibraActivity : AppCompatActivity(), CoroutineScope {

    @Inject lateinit var instance: DebugLibraInstance

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val txtLibraCache by lazy { findViewById<Typography>(R.id.txt_variant_cache) }
    private val txtLibraRemote by lazy { findViewById<Typography>(R.id.txt_variant_remote) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_libra)
        setupInjector()

        val owner = LibraOwner.Home

        txtLibraCache.text = "cache:\n${instance.variantFromCache(owner)}"

        launch {
            val result = instance.variants(owner)

            withContext(Dispatchers.Main) {
                txtLibraRemote.text = "remote:\n${result}"
            }
        }
    }

    private fun setupInjector() {
        DaggerDebugLibraComponent
            .builder()
            .baseAppComponent((applicationContext as? BaseMainApplication)?.baseAppComponent)
            .debugLibraModule(DebugLibraModule())
            .build()
            .inject(this)
    }
}
