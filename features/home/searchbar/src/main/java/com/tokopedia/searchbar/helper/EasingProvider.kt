package com.tokopedia.searchbar.helper

import androidx.annotation.NonNull

internal object EasingProvider {

    operator fun get(@NonNull ease: Ease?, elapsedTimeRate: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        return when (ease) {
            Ease.LINEAR -> elapsedTimeRate
            Ease.QUAD_IN -> getPowIn(elapsedTimeRate, 2.0)
            Ease.QUAD_OUT -> getPowOut(elapsedTimeRate, 2.0)
            Ease.QUAD_IN_OUT -> getPowInOut(elapsedTimeRate, 2.0)
            Ease.CUBIC_IN -> getPowIn(elapsedTimeRate, 3.0)
            Ease.CUBIC_OUT -> getPowOut(elapsedTimeRate, 3.0)
            Ease.CUBIC_IN_OUT -> getPowInOut(elapsedTimeRate, 3.0)
            Ease.QUART_IN -> getPowIn(elapsedTimeRate, 4.0)
            Ease.QUART_OUT -> getPowOut(elapsedTimeRate, 4.0)
            Ease.QUART_IN_OUT -> getPowInOut(elapsedTimeRate, 4.0)
            Ease.QUINT_IN -> getPowIn(elapsedTimeRate, 5.0)
            Ease.QUINT_OUT -> getPowOut(elapsedTimeRate, 5.0)
            Ease.QUINT_IN_OUT -> getPowInOut(elapsedTimeRate, 5.0)
            Ease.SINE_IN -> (1f - Math.cos(elapsedTimeRate * Math.PI / 2f)).toFloat()
            Ease.SINE_OUT -> Math.sin(elapsedTimeRate * Math.PI / 2f).toFloat()
            Ease.SINE_IN_OUT -> (-0.5f * (Math.cos(Math.PI * elapsedTimeRate) - 1f)).toFloat()
            Ease.BACK_IN -> (elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate - 1.7)).toFloat()
            Ease.BACK_OUT -> (--elapsedTimeRate * elapsedTimeRate * ((1.7 + 1f) * elapsedTimeRate + 1.7) + 1f).toFloat()
            Ease.BACK_IN_OUT -> getBackInOut(elapsedTimeRate, 1.7f)
            Ease.CIRC_IN -> (-(Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate.toDouble()) - 1)).toFloat()
            Ease.CIRC_OUT -> Math.sqrt(1f - --elapsedTimeRate * elapsedTimeRate.toDouble()).toFloat()
            Ease.CIRC_IN_OUT -> {
                if (2f.let { elapsedTimeRate *= it; elapsedTimeRate } < 1f) {
                    (-0.5f * (Math.sqrt(1f - elapsedTimeRate * elapsedTimeRate.toDouble()) - 1f)).toFloat()
                } else (0.5f * (Math.sqrt(1f - 2f.let {
                    elapsedTimeRate -= it; elapsedTimeRate
                } * elapsedTimeRate.toDouble()) + 1f)).toFloat()
            }
            Ease.BOUNCE_IN -> getBounceIn(elapsedTimeRate)
            Ease.BOUNCE_OUT -> getBounceOut(elapsedTimeRate)
            Ease.BOUNCE_IN_OUT -> {
                if (elapsedTimeRate < 0.5f) {
                    getBounceIn(elapsedTimeRate * 2f) * 0.5f
                } else getBounceOut(elapsedTimeRate * 2f - 1f) * 0.5f + 0.5f
            }
            Ease.ELASTIC_IN -> getElasticIn(elapsedTimeRate, 1.0, 0.3)
            Ease.ELASTIC_OUT -> getElasticOut(elapsedTimeRate, 1.0, 0.3)
            Ease.ELASTIC_IN_OUT -> getElasticInOut(elapsedTimeRate, 1.0, 0.45)
            Ease.EASE_IN_EXPO -> {
                Math.pow(2.0, 10 * (elapsedTimeRate - 1).toDouble()).toFloat()
            }
            Ease.EASE_OUT_EXPO -> {
                (-Math.pow(2.0, -10 * elapsedTimeRate.toDouble())).toFloat() + 1
            }
            Ease.EASE_IN_OUT_EXPO -> {
                if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
                    Math.pow(2.0, 10 * (elapsedTimeRate - 1).toDouble()).toFloat() * 0.5f
                } else (-Math.pow(2.0, (-10 * --elapsedTimeRate).toDouble()) + 2f).toFloat() * 0.5f
            }
            else -> elapsedTimeRate
        }
    }

    private fun getPowIn(elapsedTimeRate: Float, pow: Double): Float {
        return Math.pow(elapsedTimeRate.toDouble(), pow).toFloat()
    }

    private fun getPowOut(elapsedTimeRate: Float, pow: Double): Float {
        return (1.toFloat() - Math.pow(1 - elapsedTimeRate.toDouble(), pow)).toFloat()
    }

    private fun getPowInOut(elapsedTimeRate: Float, pow: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (0.5 * Math.pow(elapsedTimeRate.toDouble(), pow)).toFloat()
        } else (1 - 0.5 * Math.abs(Math.pow(2 - elapsedTimeRate.toDouble(), pow))).toFloat()
    }

    private fun getBackInOut(elapsedTimeRate: Float, amount: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        var amount = amount
        amount *= 1.525f
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (0.5 * (elapsedTimeRate * elapsedTimeRate * ((amount + 1) * elapsedTimeRate - amount))).toFloat()
        } else (0.5 * (2.let {
            elapsedTimeRate -= it; elapsedTimeRate
        } * elapsedTimeRate * ((amount + 1) * elapsedTimeRate + amount) + 2)).toFloat()
    }

    private fun getBounceIn(elapsedTimeRate: Float): Float {
        return 1f - getBounceOut(1f - elapsedTimeRate)
    }

    private fun getBounceOut(elapsedTimeRate: Float): Float {
        var elapsedTimeRate = elapsedTimeRate
        return if (elapsedTimeRate < 1 / 2.75) {
            (7.5625 * elapsedTimeRate * elapsedTimeRate).toFloat()
        } else if (elapsedTimeRate < 2 / 2.75) {
            (7.5625 * (1.5 / 2.75.let {
                elapsedTimeRate = (elapsedTimeRate - it).toFloat()
                elapsedTimeRate
            }) * elapsedTimeRate + 0.75).toFloat()
        } else if (elapsedTimeRate < 2.5 / 2.75) {
            (7.5625 * (2.25 / 2.75.let {
                elapsedTimeRate = (elapsedTimeRate - it).toFloat()
                elapsedTimeRate
            }) * elapsedTimeRate + 0.9375).toFloat()
        } else {
            (7.5625 * (2.625 / 2.75.let {
                elapsedTimeRate = (elapsedTimeRate - it).toFloat()
                elapsedTimeRate
            }) * elapsedTimeRate + 0.984375).toFloat()
        }
    }

    private fun getElasticIn(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (-(amplitude * Math.pow(2.0, 10f * 1f.let {
            elapsedTimeRate -= it; elapsedTimeRate
        }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat()
    }

    private fun getElasticOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        if (elapsedTimeRate == 0f || elapsedTimeRate == 1f) return elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return (amplitude * Math.pow(2.0, -10 * elapsedTimeRate.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) + 1).toFloat()
    }

    private fun getElasticInOut(elapsedTimeRate: Float, amplitude: Double, period: Double): Float {
        var elapsedTimeRate = elapsedTimeRate
        val pi2 = Math.PI * 2
        val s = period / pi2 * Math.asin(1 / amplitude)
        return if (2.let { elapsedTimeRate *= it; elapsedTimeRate } < 1) {
            (-0.5f * (amplitude * Math.pow(2.0, 10 * 1f.let {
                elapsedTimeRate -= it; elapsedTimeRate
            }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period))).toFloat()
        } else (amplitude * Math.pow(2.0, -10 * 1.let {
            elapsedTimeRate -= it; elapsedTimeRate
        }.toDouble()) * Math.sin((elapsedTimeRate - s) * pi2 / period) * 0.5 + 1).toFloat()
    }
}