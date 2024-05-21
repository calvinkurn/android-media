package com.tokopedia.libra

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.libra.di.DaggerLibraComponent
import com.tokopedia.libra.domain.usecase.GetLibraCacheUseCase
import com.tokopedia.libra.domain.usecase.GetLibraRemoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * A Libra request wrapper.
 *
 * How-to use:
 *
 * 1. You have to hit the fetcher first, this action will be invoked the remote service and store locally,
 * hence, the N+1 session will able to consume the data. Please execute this method either onCreate nor onResume lifecycle.
 *
 * ```
 * LibraInstance.get(context).fetch(LibraOwner)
 * ```
 *
 * 2. To fetch the variant, we have 2 options:
 * 2.1. Variant as a raw data which returns as String
 * 2.2. Variant as a data which returns as LibraResult
 * 2.3. Variant as a state (control-variant built-in handled)
 *
 * ```
 * LibraInstance.get(context).variant(LibraOwner, experiment): LibraResult
 * ```
 * or
 * ```
 * LibraInstance.get(context).variantAsString(LibraOwner, experiment): String
 * ```
 * or
 * ```
 * val state = LibraInstance.get(context).variantAsState(LibraOwner, experiment): LibraState
 *
 * when(state) {
 *   is LibraState.Control -> // control
 *   is LibraState.Variant -> // state.data.variant
 * }
 * ```
 *
 * 2024 (c) Home.
 */
class LibraInstance(context: Context) : Libra, CoroutineScope {

    @Inject lateinit var getLibraRemoteUseCase: GetLibraRemoteUseCase
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
        try {
            getLibraRemoteUseCase(owner)
        } catch (_: Throwable) {}
    }

    override fun variant(owner: LibraOwner, experiment: String): LibraResult {
        return shouldGetExperiment(owner, experiment) ?: LibraResult.empty()
    }

    override fun variantAsString(owner: LibraOwner, experiment: String): String {
        return shouldGetExperiment(owner, experiment)?.variant.orEmpty()
    }

    override fun variantAsState(owner: LibraOwner, experiment: String): LibraState {
        val result = shouldGetExperiment(owner, experiment)
        val variant = result?.variant.orEmpty()

        return when {
            variant.isEmpty() || variant == Const.CONTROL_VARIANT -> LibraState.Control
            else -> LibraState.Variant(
                LibraResult(
                    experiment = result?.experiment.orEmpty(),
                    variant = variant
                )
            )
        }
    }

    override fun clear(owner: LibraOwner) {
        getLibraCacheUseCase.clear(owner)
    }

    private fun shouldGetExperiment(owner: LibraOwner, experiment: String): LibraResult? {
        return shouldGetExperimentList(owner).firstOrNull { it.experiment == experiment }
    }

    private fun shouldGetExperimentList(owner: LibraOwner): List<LibraResult> {
        return getLibraCacheUseCase(owner).experiments
    }

    internal object Const {
        const val CONTROL_VARIANT = "control"
    }

    companion object {
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
