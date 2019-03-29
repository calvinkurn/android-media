package com.tokopedia.groupchat.chatroom.domain.pojo.poll

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Statistic {

    @SerializedName("total_voter")
    @Expose
    var totalVoter: Int = 0
    @SerializedName("statistic_options")
    @Expose
    var statisticOptions: List<StatisticOption> = ArrayList()

}
