package com.tokopedia.analyticsdebugger.cassava.data

/**
 * @author by furqan on 20/04/2021
 */
object ThanosJourneyList {
    val journeyList: List<Pair<String, String>> =
        arrayListOf(
            Pair("150", "User/Account : Home Account - Click Profile Page"),
            Pair("147", "User/Account : Inactive PN - Expedited Flow - Go to Regular Flow"),
            Pair("146", "User/Account : Inactive PN - Expedited Flow"),
            Pair("145", "User/Account : Inactive PN - Expedited Flow - Can't Access Email"),
            Pair("142", "Communication and Media : Chat - Product Card Impression"),
            Pair("141", "Communication and Media : Chat - Click ATC Product Card"),
            Pair("125", "Communication and Media : Chat - Report User"),
            Pair("117", "Communication and Media : Chat - Follow Shop"),
            Pair("114", "Communication and Media : Chat - SRW"),
            Pair("88", "Communication and Media : Chat - Send Chat"),
            Pair("67", "Search : SRP"),
            Pair("56", "Physical Goods : PDP Main transaction flow"),
            Pair("51", "Digital Goods : DG - Transaction Flow"),
            Pair("47", "Purchase Platform : PP - Transaction (OCC)"),
            Pair("44", "Purchase Platform : PP - Transaction (Regular Checkout)"),
            Pair("42", "User/Account: Login Biometrics"),
            Pair("33", "Communication and Media : TopChat - SRW"),
            Pair("30", "Communication and Media : TopChat - Notif Center"),
            Pair("25", "Communication and Media : TopChat - Report Chat"),
            Pair("23", "Communication and Media : TopChat - Attach and Send"),
            Pair("15", "Communication and Media : TopChat - Order Creation"),
            Pair("11", "Communication and Media : TopChat - SRW"),
            Pair("7", "Communication and Media : Search Result Page - Impression & Click")
        )
}
