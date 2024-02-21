---
title: "Create Feed Post"
labels:
- feed
- ccp
- createpost
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)[Shruti Agarwal (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/608fdbe3c87b550069a00200?ref=confluence)[Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)[Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) [Pandu Tyas Baghaskoro](https://tokopedia.atlassian.net/wiki/people/62d49480657fc166e25f259b?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 06 Aug 2019 / <!--start status:GREY-->MA-3.38<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD - Initial Content Creation Assessment](/wiki/spaces/CN/pages/1166182376/PRD+-+Initial+Content+Creation+Assessment)[Seller Content Creation Revamp (Product Tagging) on Main App - 210705](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1448682105/Seller+Content+Creation+Revamp+Product+Tagging+on+Main+App+-+210705)[Content Creation for Non-seller (UGC) - 220127](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1894983237/Content+Creation+for+Non-seller+UGC+-+220127) |
| Module Location | `features.createpost` | `features/content/createpost` |

## Table of Contents

<!--toc-->

## Release
 Notes (max 5 latest release notes)

<!--start expand:Dec 13, 2022 (MA-3.202)-->
###### *-* [*[Feed] Crash after success upload post as UGC*](https://tokopedia.atlassian.net/browse/AN-44936)
<!--end expand-->

<!--start expand:Dec 2, 2022 (MA-3.201)-->
###### *-* [*[CCP] UGC Cant Create Feed Post*](https://tokopedia.atlassian.net/browse/AN-44237)
<!--end expand-->

<!--start expand:Nov 16, 2022 (MA-3.200)-->
###### *-* [*[CCP - Android] Non-seller can see "+ Buat Live" entry point on User Profile page*](https://tokopedia.atlassian.net/browse/AN-42321)
<!--end expand-->

<!--start expand:Nov 10, 2022 (MA-3.199)-->
###### *-* [*[CCP - Android] Product source picker*](https://tokopedia.atlassian.net/browse/AN-39890)- [*[CCP - Android] Non-seller entry point and account switching*](https://tokopedia.atlassian.net/browse/AN-39888)
<!--end expand-->

<!--start expand:Nov 4, 2022 (MA-3.198)-->
###### *-* [*[CCP - Android] Global Tokopedia search*](https://tokopedia.atlassian.net/browse/AN-39889)- [*[CCP - Android] Selected products screen*](https://tokopedia.atlassian.net/browse/AN-39895)- [*[CCP] Enable Multiple Selection in Feed x Play Product Picker*](https://tokopedia.atlassian.net/browse/AN-40176)
<!--end expand-->

## Overview

Create Post Feed is a module that used to create a post that will be shown in Feed tab on Homepage and in Feed tab on Shop Page also User Profile.



### Background

The more customers understand about the product, the more chance for them to purchase the product. Feed post is one of the possible solution that help user/seller to promote their product by uploading photos or videos of their products. Create post feed allow user to showcase & promote their products in graphic + caption text form.

### Project Description

Create post is a feature where sellers / users are able to post an image, video and carousel images to promote their own products or even share anything that will attract other users interest to check on their shop or profile. This feature is available on both MainApp & SellerApp. To create a post, sellers / users can access it from Feed on Homepage or Shop Page.

To create a post, users need to :



1. Choose the media (images / video) => ImagePicker Module.
2. Tag Product (optional).
3. Write the description / caption for the post (optional).
4. Post

[feed-create-post-entry-point.webm](/wiki/download/attachments/2109604471/feed-create-post-entry-point.webm?version=1&modificationDate=1674757588789&cacheVersion=1&api=v2&width=340)

## Tech Stack

- **Dynamic Features** **(On-Demand & Install-Time)**: not all users will use this feature. So to save the size of Tokopedia Apps in user’s mobile device, we setup this features as DF On-Demand & DF Install Time for Android device with SDK between 21 - 23. [Click here to see the official documentation.](https://developer.android.com/guide/playcore/feature-delivery)

## Navigation

[feed-create-post-entry-point.webm](/wiki/download/attachments/2109604471/feed-create-post-entry-point.webm?version=1&modificationDate=1674757588789&cacheVersion=1&api=v2&width=340)

[ma\_-\_create\_post\_page\_-\_from\_user\_profile.mov](/wiki/download/attachments/2109604471/ma_-_create_post_page_-_from_user_profile.mov?version=1&modificationDate=1675646488780&cacheVersion=1&api=v2&width=340)

[sa\_-\_create\_post\_page\_-\_from\_shop\_page.mov](/wiki/download/attachments/2109604471/sa_-_create_post_page_-_from_shop_page.mov?version=1&modificationDate=1675646616436&cacheVersion=1&api=v2&width=340)

[ma\_-\_edit\_post\_page\_-\_from\_feed.mov](/wiki/download/attachments/2109604471/ma_-_edit_post_page_-_from_feed.mov?version=1&modificationDate=1675646662653&cacheVersion=1&api=v2&width=340)

[sa\_-\_edit\_post\_page\_-\_from\_shop\_page.mov](/wiki/download/attachments/2109604471/sa_-_edit_post_page_-_from_shop_page.mov?version=1&modificationDate=1675646715644&cacheVersion=1&api=v2&width=340)

## How-to

To open Create Feed Post, you can use the `RouteManager.route()` with applink `tokopedia://affiliate/create_post_v2` or directly use `ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2`



```
fun openCreatePost() {
    RouteManager.route(requireContext(), ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
}
```

In case you need to send parameters, you need to add dependency to `content_common` as all the parameter field are stored in this module. After that you can get the intent from `RouteManager.getIntent()` and send the parameters by using extras.



```
fun openPostEditPage(currentCaption: String, postId: String, authorId: String) {
    val postData = CreatePostViewModel()
    postData.caption = currentCaption
    postData.postId = postId
    postData.editAuthorId = authorId
    
    val intent = RouteManager.getIntent(requireContext(), ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
    intent.putExtra(CreatePostViewModel.TAG, postData)
    intent.putExtra(KEY_AUTHOR_TYPE, TYPE_USER)
    
    startActivity(intent)
}
```

## GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| Get Content Form | [[Feeds][GQL] Get Content Form (feed\_content\_form)](/wiki/spaces/CN/pages/1856438559)  | This GQL Query is used to get the post configuration, e.g. the authors, maxProductTagged allowed, etc. |
| Affiliate Post Quota | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/901688377/TECH+REPO+CNA+GQL+Affiliate#id-%5BTECH%5D%5BREPO_CNA%5D%5BGQL%5DAffiliate-affiliatePostQuota>  | This GQL Query is used to get affiliate post quota if user is affilate |
| Submit Post Flow GQL | *To be shared* | Submit post flow gql is part of createpost\_common module. Please refer to that documentation.<br/> |
| Product Tag Flow GQL | [Content Common](/wiki/spaces/PA/pages/2158464917/Content+Common)  | Product tag flow gql is part of content\_common module. Please refer to that documentation. |

## Action Items

- Increase unit test coverage

## Useful Links

- [Figma](https://www.figma.com/file/xol2pH7zBm02Cl8dzznVNb/%5BUI%2FUX---M---Feed%5D-Seller-%26-Buyer-Content-Creation?node-id=27%3A0&viewport=237%2C-1086%2C0.08858148753643036)
- [MyNakama](https://mynakama.tokopedia.com/datatracker/requestdetail/view/2903)

## FAQ

<!--start expand:Can we edit media / products after posting?-->
No, the edit post is only applicable for description/caption only.
<!--end expand-->

<!--start expand:Can I tag products from global search Tokopedia?-->
No, tagged products must be products of the user's shop. So it is not possible to tag products outside of the poster’s product
<!--end expand-->

<!--start expand:How to post as User Generated Content (UGC)?-->
Currently, to post as UGC, your Tokopedia account must be whitelisted by Tokopedia team, also need to create username and accept the terms and conditions (TnC) in User Profile Page
<!--end expand-->

<!--start expand:What is the credential for UGC?-->
Please reach out to Minion Lance ([Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) ([Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Shruti Agarwal (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/608fdbe3c87b550069a00200?ref=confluence))
<!--end expand-->

