package com.tokopedia.applink

import android.net.Uri
import com.tokopedia.applink.DeepLinkChecker.BLOG
import com.tokopedia.applink.DeepLinkChecker.BROWSE
import com.tokopedia.applink.DeepLinkChecker.CATALOG
import com.tokopedia.applink.DeepLinkChecker.CATEGORY
import com.tokopedia.applink.DeepLinkChecker.CONTENT
import com.tokopedia.applink.DeepLinkChecker.DISCOVERY_PAGE
import com.tokopedia.applink.DeepLinkChecker.ETALASE
import com.tokopedia.applink.DeepLinkChecker.FLIGHT
import com.tokopedia.applink.DeepLinkChecker.GROUPCHAT
import com.tokopedia.applink.DeepLinkChecker.HOT
import com.tokopedia.applink.DeepLinkChecker.HOT_LIST
import com.tokopedia.applink.DeepLinkChecker.INVOICE
import com.tokopedia.applink.DeepLinkChecker.ORDER_LIST
import com.tokopedia.applink.DeepLinkChecker.OTHER
import com.tokopedia.applink.DeepLinkChecker.PELUANG
import com.tokopedia.applink.DeepLinkChecker.PLAY
import com.tokopedia.applink.DeepLinkChecker.PRODUCT
import com.tokopedia.applink.DeepLinkChecker.PROFILE
import com.tokopedia.applink.DeepLinkChecker.PROMO
import com.tokopedia.applink.DeepLinkChecker.RECHARGE
import com.tokopedia.applink.DeepLinkChecker.RECOMMENDATION
import com.tokopedia.applink.DeepLinkChecker.REFERRAL
import com.tokopedia.applink.DeepLinkChecker.SALE
import com.tokopedia.applink.DeepLinkChecker.SHOP
import com.tokopedia.applink.DeepLinkChecker.SMCREFERRAL
import com.tokopedia.applink.DeepLinkChecker.TOKOPOINT
import com.tokopedia.applink.DeepLinkChecker.TOPPICKS
import com.tokopedia.applink.DeepLinkChecker.WALLET_OVO

class DeeplinkMatcher() {
    private val EQ = true
    private val GT = false

    var matcherList: List<Pair<Pattern, Int>>

    init {
        matcherList = mutableListOf<Pair<Pattern, Int>>().apply {
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
            add(Pattern(GT,0, mapOf(0 to "order-list")) to ORDER_LIST)
            add(Pattern(GT, 0, mapOf(0 to "activation.pl")) to OTHER)
            add(Pattern(EQ, 2, mapOf(0 to "rekomendasi")) to RECOMMENDATION)
            add(Pattern(EQ, 1, mapOf(0 to "rekomendasi")) to RECOMMENDATION)
            add(Pattern(EQ, 1, null) to SHOP)
            add(Pattern(EQ, 2, null) to PRODUCT)
        }
    }

    fun match(uri: Uri): Int {
        val linkSegment = uri.getPathSegments()
        val linkSegmentSize = linkSegment.size
        for ((matcherPattern, result) in matcherList) {
            if (
                !if (matcherPattern.isSegmentSizeEqual) {
                    linkSegmentSize == matcherPattern.segmentSize
                } else {
                    linkSegmentSize > matcherPattern.segmentSize
                }
            ) {
                continue
            }
            var isMatch = true
            matcherPattern.patternIndex?.let { it ->
                for ((index, pattern) in it) {
                    if (!linkSegment.get(index).equals(pattern, false)) {
                        isMatch = false
                        break
                    }
                }
            }
            if (isMatch) {
                return result
            }
        }
        return OTHER
    }
}

class Pattern(
    var isSegmentSizeEqual: Boolean = false,
    var segmentSize: Int = 1,
    var patternIndex: Map<Int, String>?)