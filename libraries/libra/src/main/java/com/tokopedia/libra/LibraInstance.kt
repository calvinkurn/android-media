package com.tokopedia.libra

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.libra.di.DaggerLibraComponent
import com.tokopedia.libra.domain.usecase.GetLibraCacheUseCase
import com.tokopedia.libra.domain.usecase.SetLibraUseCase
import com.tokopedia.libra.domain.model.ItemLibraUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
    }

    override suspend fun fetch(owner: LibraOwner) {
        setLibraUseCase(owner)
    }

    override fun variant(owner: LibraOwner, experiment: String): String {
        return shouldGetExperiment(owner, experiment)?.variant.orEmpty()
    }

    override fun variantAsState(owner: LibraOwner, experiment: String): LibraState {
        val variant = shouldGetExperiment(owner, experiment)?.variant.orEmpty()

        return when {
            variant.isEmpty() -> LibraState.None
            variant == CONTROL_VARIANT -> LibraState.Control
            else -> LibraState.Variant(variant)
        }
    }

    override fun clear(owner: LibraOwner) {
        getLibraCacheUseCase(owner, true)
    }

    private fun shouldGetExperiment(owner: LibraOwner, experiment: String): ItemLibraUiModel? {
        return shouldGetExperimentList(owner).firstOrNull { it.experiment == experiment }
    }

    private fun shouldGetExperimentList(owner: LibraOwner): List<ItemLibraUiModel> {
        return getLibraCacheUseCase(owner).experiments
    }

    companion object {
        private const val CONTROL_VARIANT = "control"
        @Volatile var libra: LibraInstance? = null

        fun get(context: Context): LibraInstance {
            return synchronized(this) {
                libra ?: LibraInstance(context).also {
                    libra = it
                }
            }
        }
    }
}
