package com.tokopedia.talk.analytics.util

import android.content.Context
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig

class TalkMockResponse : MockModelConfig() {

    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(KEY_CONTAINS_DISCUSSION_INBOX, MOCK_RESPONSE_DISCUSSION_INBOX, FIND_BY_CONTAINS)
        addMockResponse(KEY_CONTAINS_DISCUSSION_BY_QUESTION, MOCK_RESPONSE_DISCUSSION_BY_QUESTION, FIND_BY_CONTAINS)
        return this
    }

    companion object {
        private const val KEY_CONTAINS_DISCUSSION_INBOX = "discussionInbox"
        private const val KEY_CONTAINS_DISCUSSION_BY_QUESTION = "discussionDataByQuestionID"
        private const val MOCK_RESPONSE_DISCUSSION_BY_QUESTION = """
        [
          {
            "data": {
              "discussionDataByQuestionID": {
                "shopID": "1479278",
                "shopURL": "https://www.tokopedia.com/tumblersbux",
                "productID": "1079655705",
                "productName": "ayam goreng upin ipin - 4",
                "thumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/8/4/35743754-29fa-420c-9abc-fbfd155c9e66.jpg",
                "url": "https://www.tokopedia.com/tumblersbux/ayam-goreng-upin-ipin-4",
                "maxAnswerLength": 500,
                "question": {
                  "questionID": "296180291",
                  "content": "asdasdadadaaaaBisa dikirim hari ini ga?",
                  "maskedContent": "",
                  "userName": "Gerard",
                  "userThumbnail": "https://accounts.tokopedia.com/image/v1/u/77356835/user_thumbnail/desktop",
                  "userID": "77356835",
                  "createTime": "2020-09-23T12:24:55Z",
                  "createTimeFormatted": "23 Sep 2020",
                  "state": {
                    "allowReply": true,
                    "allowUnmask": false,
                    "allowReport": true,
                    "allowDelete": false,
                    "allowFollow": false,
                    "isFollowed": true,
                    "isMasked": false,
                    "isYours": false
                  },
                  "totalAnswer": 6,
                  "answer": [
                    {
                      "answerID": "586010093",
                      "content": "test",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-10-02T17:59:08Z",
                      "createTimeFormatted": "2 Okt 2020",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    },
                    {
                      "answerID": "586636378",
                      "content": "test",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-10-09T13:25:59Z",
                      "createTimeFormatted": "9 Okt 2020",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    },
                    {
                      "answerID": "586884605",
                      "content": "okokok",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-10-12T14:19:49Z",
                      "createTimeFormatted": "12 Okt 2020",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    },
                    {
                      "answerID": "586970196",
                      "content": "test.",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-10-13T13:17:07Z",
                      "createTimeFormatted": "13 Okt 2020",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    },
                    {
                      "answerID": "586970235",
                      "content": "test.",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-10-13T13:17:30Z",
                      "createTimeFormatted": "13 Okt 2020",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    },
                    {
                      "answerID": "588696382",
                      "content": "yesss",
                      "maskedContent": "",
                      "userName": "Tumbler Starbucks 123",
                      "userThumbnail": "https://ecs7-p.tokopedia.net/img/cache/215-square/shops-1/2020/11/20/1479278/1479278_46ddd992-79bd-479c-bd28-816932dae828.jpg",
                      "userID": "12299749",
                      "isSeller": true,
                      "createTime": "2020-11-03T09:21:09Z",
                      "createTimeFormatted": "3 minggu lalu",
                      "likeCount": 0,
                      "state": {
                        "isMasked": false,
                        "isLiked": false,
                        "allowLike": false,
                        "allowUnmask": false,
                        "allowReport": false,
                        "allowDelete": true,
                        "isYours": true
                      },
                      "attachedProductCount": 0,
                      "attachedProduct": []
                    }
                  ]
                }
              }
            }
          }
        ]
        """

        private const val MOCK_RESPONSE_DISCUSSION_INBOX = """
        [
          {
            "data": {
              "discussionInbox": {
                "userName": "Try Sugihartoo",
                "shopID": "1479278",
                "shopName": "Tumbler Starbucks 123",
                "inboxType": "seller",
                "sellerUnread": 0,
                "buyerUnread": 0,
                "hasNext": true,
                "inbox": [
                  {
                    "inboxID": "643463982",
                    "questionID": "296180291",
                    "isMasked": false,
                    "content": "asdasdadadaaaaBisa dikirim hari ini ga?",
                    "lastReplyTime": "23 Sep 2020",
                    "totalAnswer": 6,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "1079655705",
                    "productName": "ayam goreng upin ipin - 4",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/8/4/35743754-29fa-420c-9abc-fbfd155c9e66.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/ayam-goreng-upin-ipin-4"
                  },
                  {
                    "inboxID": "643463878",
                    "questionID": "296180246",
                    "isMasked": false,
                    "content": "Bisa dikirim hari ini ga?Hai, barang ini ready ga?",
                    "lastReplyTime": "23 Sep 2020",
                    "totalAnswer": 0,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "1079655705",
                    "productName": "ayam goreng upin ipin - 4",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/8/4/35743754-29fa-420c-9abc-fbfd155c9e66.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/ayam-goreng-upin-ipin-4"
                  },
                  {
                    "inboxID": "641731612",
                    "questionID": "295437739",
                    "isMasked": false,
                    "content": "ready 10 pcs ?",
                    "lastReplyTime": "15 Sep 2020",
                    "totalAnswer": 2,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "1025767025",
                    "productName": "dog statuet",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/7/14/75b38265-b7d2-41ec-9dbb-10607eb98545.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/dog-statuet"
                  },
                  {
                    "inboxID": "641444764",
                    "questionID": "295315098",
                    "isMasked": false,
                    "content": "testttttttTerima kasih!",
                    "lastReplyTime": "8 Sep 2020",
                    "totalAnswer": 0,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "1127798750",
                    "productName": "tukiss",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/VqbcmM/2020/8/26/b894bb8f-b8ab-4feb-8011-cc7e7aaa62c9.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/tukiss"
                  },
                  {
                    "inboxID": "641425689",
                    "questionID": "295306895",
                    "isMasked": false,
                    "content": "ada port vga nya gk gan??",
                    "lastReplyTime": "8 Sep 2020",
                    "totalAnswer": 0,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "203354870",
                    "productName": "Produk Software",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2016/12/17/12299749/12299749_77083b20-4643-4485-8584-3e4059ef8119_1600_1600.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/produk-software"
                  },
                  {
                    "inboxID": "640852613",
                    "questionID": "295058762",
                    "isMasked": false,
                    "content": "joko kebluk hahah",
                    "lastReplyTime": "4 Sep 2020",
                    "totalAnswer": 0,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "408078353",
                    "productName": "Handphone Nokia Warna Hitam Bukan Biru Bukan Merah Juga Nokia ya bukan",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/11/23/12299749/12299749_db9e5e59-66fd-4481-b2e5-4ef27eb9b44f_700_700.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/handphone-nokia-warna-hitam-bukan-biru-bukan-merah-juga-nokia-ya-bukan"
                  },
                  {
                    "inboxID": "626493617",
                    "questionID": "288804663",
                    "isMasked": false,
                    "content": "beneran harganya atau iseng?",
                    "lastReplyTime": "17 Jun 2020",
                    "totalAnswer": 4,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "69748459",
                    "productName": "Barang ini sudah dihapus",
                    "productThumbnail": "",
                    "productURL": ""
                  },
                  {
                    "inboxID": "626124192",
                    "questionID": "288640760",
                    "isMasked": false,
                    "content": "ini beneran harganya?",
                    "lastReplyTime": "15 Jun 2020",
                    "totalAnswer": 5,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "188187710",
                    "productName": "Produk Buat Dikosongkan",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2016/12/17/12299749/12299749_77083b20-4643-4485-8584-3e4059ef8119_1600_1600.jpg",
                    "productURL": "https://www.tokopedia.com/tumblersbux/produk-buat-dikosongkan"
                  },
                  {
                    "inboxID": "614858232",
                    "questionID": "283582378",
                    "isMasked": false,
                    "content": "test pertanyaan 2",
                    "lastReplyTime": "11 Mei 2020",
                    "totalAnswer": 6,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "408083893",
                    "productName": "hai there",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/2/13/12299749/12299749_6a85a642-6e99-4c0a-9bf6-460c83135f61_1156_1156",
                    "productURL": "https://www.tokopedia.com/tumblersbux/hai-there"
                  },
                  {
                    "inboxID": "614857310",
                    "questionID": "283581972",
                    "isMasked": false,
                    "content": "tetstt",
                    "lastReplyTime": "11 Mei 2020",
                    "totalAnswer": 1,
                    "isUnread": false,
                    "answererThumbnail": [],
                    "productID": "408083893",
                    "productName": "hai there",
                    "productThumbnail": "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/2/13/12299749/12299749_6a85a642-6e99-4c0a-9bf6-460c83135f61_1156_1156",
                    "productURL": "https://www.tokopedia.com/tumblersbux/hai-there"
                  }
                ]
              }
            }
          }
        ]
        """
    }


}