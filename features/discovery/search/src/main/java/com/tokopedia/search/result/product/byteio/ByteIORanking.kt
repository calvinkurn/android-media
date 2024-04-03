package com.tokopedia.search.result.product.byteio

interface ByteIORanking {

    fun setRank(value: Int)

    fun getRank(): Int

    fun setItemRank(value: Int)

    fun getItemRank(): Int
}

data class ByteIORankingImpl(
    private var rank: Int = 0,
    private var itemRank: Int = 0,
): ByteIORanking {

    override fun setRank(value: Int) {
        this.rank = value
    }

    override fun getRank(): Int = rank

    override fun setItemRank(value: Int) {
        this.itemRank = value
    }

    override fun getItemRank(): Int = this.itemRank
}
