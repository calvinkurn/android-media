package com.tokopedia.promousage.domain.entity


/**
 * Voucher Source represent from which source the voucher originates from.
 * UserInput:
 *      Means the voucher source is coming from user manually input voucher code.
 *      It will make voucher widget will display voucher code (e.g TOKOPEDIAXBCA) on bottom right of voucher cardview
 * Promo:
 *      It will not display voucher code on bottom right of voucher cardview
 */
sealed class VoucherSource {
    object Promo: VoucherSource()
    data class UserInput(val voucherCode: String): VoucherSource()
}
