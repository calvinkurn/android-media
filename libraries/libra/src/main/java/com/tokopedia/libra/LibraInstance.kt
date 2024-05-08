package com.tokopedia.libra

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.libra.di.DaggerLibraComponent
import com.tokopedia.libra.domain.GetLibraCacheUseCase
import com.tokopedia.libra.domain.SetLibraUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LibraInstance(context: Context) : Libra, CoroutineScope {

    @Inject lateinit var setLibraUseCase: SetLibraUseCase
    @Inject lateinit var getLibraCacheUseCase: GetLibraCacheUseCase

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default

    init {
        DaggerLibraComponent
            .builder()
            .baseAppComponent((context.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)

        fetch()
    }

    override fun getVariant(owner: LibraOwner, experiment: String): String {
        return getLibraCacheUseCase(owner)
            .experiments
            .firstOrNull { it.experiment == experiment }
            ?.variant
            .orEmpty()
    }

    override fun cleanUp() {
        if (!coroutineContext.isActive) return
        cancel()
    }

    private fun fetch() {
        // As of now, the LibraParameters service only support for home tribe.
        shouldRequestAndStoreToCache(LibraOwner.Home)
    }

    private fun shouldRequestAndStoreToCache(owner: LibraOwner) {
        launch {
            setLibraUseCase(owner)
        }
    }

    companion object {

        @Volatile var libra: LibraInstance? = null

        fun getInstance(context: Context): LibraInstance {
            return synchronized(this) {
                libra ?: LibraInstance(context).also {
                    libra = it
                }
            }
        }
    }
}
