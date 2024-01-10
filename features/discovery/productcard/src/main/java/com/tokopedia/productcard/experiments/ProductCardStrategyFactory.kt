package com.tokopedia.productcard.experiments

import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import com.tokopedia.productcard.R
import com.tokopedia.productcard.experiments.ProductCardStrategyFactory.ReimagineTemplate.CAROUSEL
import com.tokopedia.productcard.experiments.ProductCardStrategyFactory.ReimagineTemplate.REGULAR

internal object ProductCardStrategyFactory {

    fun create(productCardView: ViewGroup, attrs: AttributeSet? = null): ProductCardStrategy {
        attrs ?: return GridViewStrategy(productCardView)

        val typedArray = productCardView.context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return GridViewStrategy(productCardView)

        return try {
            tryCreateStrategy(typedArray, productCardView)
        } catch (_: Throwable) {
            GridViewStrategy(productCardView)
        } finally {
            typedArray.recycle()
        }
    }

    private fun tryCreateStrategy(typedArray: TypedArray, productCardView: ViewGroup) =
        if (isReimagine(typedArray))
            ReimagineTemplate
                .of(reimagineTemplate(typedArray))
                .create(productCardView)
        else GridViewStrategy(productCardView)

    private fun isReimagine(typedArray: TypedArray) =
        typedArray.getBoolean(R.styleable.ProductCardView_reimagine, false)
            && ProductCardExperiment.isReimagine()

    private fun reimagineTemplate(typedArray: TypedArray) =
        typedArray.getInt(R.styleable.ProductCardView_reimagine_template, REGULAR.value)

    private enum class ReimagineTemplate(val value: Int) {
        REGULAR(1) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy {
                return ReimagineGridViewStrategy(productCardView)
            }
        },

        CAROUSEL(2) {
            override fun create(productCardView: ViewGroup): ProductCardStrategy {
                return ReimagineGridViewStrategy(productCardView)
            }
        };

        abstract fun create(productCardView: ViewGroup): ProductCardStrategy

        companion object {
            fun of(value: Int): ReimagineTemplate =
                ReimagineTemplate.values().find { it.value == value } ?: REGULAR
        }
    }
}
