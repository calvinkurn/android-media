package com.tokopedia.applink

import android.net.Uri

class DeeplinkMatcher() {
    private val EQ = true
    private val GT = false

    private val matcherList = mutableListOf<Pair<Pattern, Int>>()

    companion object {
        val OTHER = -1
        val BROWSE = 0
        val HOT = 1
        val CATALOG = 2
        val PRODUCT = 3
        val SHOP = 4
        val TOPPICKS = 5
        val HOT_LIST = 6
        val CATEGORY = 7
        val HOME = 8
        val PROMO = 9
        val ETALASE = 10
        val APPLINK = 11
        val INVOICE = 12
        val ACCOUNTS = 13
        val RECHARGE = 14
        val BLOG = 15
        val PELUANG = 16
        val DISCOVERY_PAGE = 17
        val FLIGHT = 18
        val REFERRAL = 19
        val TOKOPOINT = 20
        val GROUPCHAT = 21
        val SALE = 22
        val WALLET_OVO = 23
        val PLAY = 24
        val PROFILE = 25
        val CONTENT = 26
        val SMCREFERRAL = 27
    }

    init {
        matcherList.apply {
            add(Pattern(GT, 0, mapOf(0 to "play")) to PLAY)
            add(Pattern(GT, 0, mapOf(0 to "groupchat")) to GROUPCHAT)
            add(Pattern(GT, 0, mapOf(0 to "flight")) to FLIGHT)
            add(Pattern(GT, 0, mapOf(0 to "promo")) to PROMO)
            add(Pattern(GT, 0, mapOf(0 to "sale")) to SALE)
            add(Pattern(EQ, 1, mapOf(0 to "invoice.pl")) to INVOICE)
            add(Pattern(GT, 0, mapOf(0 to "blog")) to BLOG)
            add(Pattern(GT, 0, mapOf(0 to "peluang")) to PELUANG)
            add(Pattern(GT, 0, mapOf(0 to "peluang.pl")) to PELUANG)
            add(Pattern(GT, 0, mapOf(0 to "p")) to CATEGORY)
            add(Pattern(GT, 0, mapOf(0 to "search")) to BROWSE)
            add(Pattern(EQ, 1, mapOf(0 to "hot")) to HOT_LIST)
            add(Pattern(GT, 1, mapOf(0 to "hot")) to HOT)
            add(Pattern(GT, 0, mapOf(0 to "catalog")) to CATALOG)
            add(Pattern(EQ, 2, mapOf(0 to "b")) to DISCOVERY_PAGE)
            add(Pattern(EQ, 2, mapOf(0 to "discovery")) to DISCOVERY_PAGE)
            add(Pattern(EQ, 1, mapOf(0 to "pulsa")) to RECHARGE)
            add(Pattern(GT, 0, mapOf(0 to "toppicks")) to TOPPICKS)
            add(Pattern(EQ, 3, mapOf(1 to "etalase")) to ETALASE)
            add(Pattern(GT, 0, mapOf(0 to "referral")) to REFERRAL)
            add(Pattern(GT, 0, mapOf(0 to "tokopoints")) to TOKOPOINT)
            add(Pattern(GT, 0, mapOf(0 to "ovo")) to WALLET_OVO)
            add(Pattern(GT, 1, mapOf(0 to "people")) to PROFILE)
            add(Pattern(GT, 0, mapOf(0 to "content")) to CONTENT)
            add(Pattern(GT, 0, mapOf(0 to "kupon-thr")) to SMCREFERRAL)
            add(Pattern(GT, 0, mapOf(0 to "emas")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "reksa-dana")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "bantuan")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "events")) to OTHER)
            add(Pattern(GT, 1, mapOf(0 to "terms", 1 to "merchantkyc")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "contact-us")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "about")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "reset.pl")) to OTHER)
            add(Pattern(GT, 0, mapOf(0 to "activation.pl")) to OTHER)
            add(Pattern(EQ, 1, null) to SHOP)
            add(Pattern(EQ, 2, null) to PRODUCT)
        }
    }

    fun match(uri: Uri): Int {
        val linkSegment = uri.getPathSegments()
        val linkSegmentSize = linkSegment.size
        for ((matcherPattern, result) in matcherList) {
            if (
                ! if (matcherPattern.isSegmentSizeEqual) {
                    linkSegmentSize == matcherPattern.segmentSize
                } else {
                    linkSegmentSize > matcherPattern.segmentSize
                }
            ) {
                continue
            }
            matcherPattern.patternIndex?.let { it ->
                var isMatch = true
                for ((index, pattern) in it) {
                    if (!linkSegment.get(index).equals(pattern, false)) {
                        isMatch = false
                        break
                    }
                }
                if (isMatch) {
                    return result
                }
            }
        }
        return OTHER
    }
}

class Pattern(
    var isSegmentSizeEqual: Boolean = false,
    var segmentSize: Int = 1,
    var patternIndex: Map<Int, String>?)