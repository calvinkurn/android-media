---
title: "Analytic"
labels:
- analytics
- content
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence) [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 25 Aug 2023 / <!--start status:BLUE-->MA-3.231<!--end status--> <!--start status:GREEN-->SA-2.161<!--end status-->  |
| Module type | <!--start status:PURPLE-->JAVA-LIBRARY<!--end status-->  |
| Module Location | `features.contentAnalytic` | `features/content/analytic` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:15 Sept 2023 (MA-3.234/SA-2.164)-->
Add more constant in Follow Recommendation PR

<https://github.com/tokopedia/android-tokopedia-core/pull/34618>
<!--end expand-->

<!--start expand:25 Aug 2023 (MA-3.231/SA-2.161)-->
First release

<https://github.com/tokopedia/android-tokopedia-core/pull/33992>
<!--end expand-->

## Overview

`Analytic` module under `features/content` directory is a dedicated module for storing all the necessary information related to analytic (constant, helper, etc).

### Background

Content has a lot of module and each module has its own analytic implementation. The analytic implementation across our modules has a lot of similarities since the pattern is almost the same. there’s a lot of same constant defined in each modules and raising confusion among developers. That’s why we create a dedicated module for storing all the common analytic classes for all content’s modules.

### Project Description

`Analytic` consists of 6 main classes for storing constant:



| **Class** | **Description** | **Example Value** |
| --- | --- | --- |
| BusinessUnit | contains BusinessUnit possible value | - play<br/>- content<br/> |
| CurrentSite | contains CurrentSite possible value | - tokopediamarketplace<br/>- tokopediaseller<br/> |
| Event | contains Event possible value | - openScreen<br/>- clickContent<br/>- viewGroupChat<br/>- …<br/> |
| EventCategory | contains EventCategory possible value | - groupchat room<br/>- unified feed<br/> |
| Key | contains possible analytic key | - event<br/>- eventLabel<br/>- businessUnit<br/>- trackerId<br/>- …<br/> |
| Value | contains possible reusable analytic value | - user<br/>- shop<br/>- follow<br/>- unfollow<br/>- …<br/> |

Besides constant, Android CMP developers are required to use analytic manager class called `ContentAnalyticManager` to send trackers within content modules. The reason is to centralize all content tracker behavior & make developers easier to use tracker. There are number of functions that you can use to help you sending trackers:

| **Sender Function** | **Description** |
| --- | --- |
| sendEvent() | send a basic event tracker |
| sendViewContent() | send a view content tracker with event : `viewContentIris` |
| sendClickContent() | send a click content tracker with event : `clickContent` |
| sendOpenScreen() | send an open screen tracker |
| sendEEPromotions() | send an Enhance Ecommerce tracker with promotions type |
| sendEEProduct() | send an Enhance Ecommerce tracker with products type |

Note : All the functions has `customFields` arguments where you can use this to add additional fields based on your use case.

There are also number of helper function that can be used alongside with functions that we have mentioned above:

| **Helper Function** | **Description** |
| --- | --- |
| concatLabels() | concat a list of label you provides and add `-` as separate symbol between labels (e.g. `123 - seller - 456`) |
| concatLabelsWithAuthor() | concat a list of label you provide with author information (`{id} - {author_type}`) as the prefix |
| impressOnlyOnce() | helps you to make sure your impression is only hit once by providing the key to differentiate your impressions with another |
| clearAllImpression() | resets all the impression key, so you can start your impression from beginning again |
| clearImpression() | reset a specific impression key |

Please refer to `How-to` section for detail implementation on how to use these functions.

## How-to

1. Add analytic dependency in your `build.gradle` module



```
dependencies {
    ...
    implementation projectOrAar(rootProject.ext.features.contentAnalytic)
    ...
}
```
2. Please make sure you have `UserSessionInterface` injector within your Dagger Component.
```
@Module
class YourModule {

    @Provides
    @YourScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
```

3. Inject `ContentAnalyticManager.Factory` to your analytic class & initiate `ContentAnalyticManager`
```
class YourTrackerImpl @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory
) {
    
    private val analyticManager = analyticManagerFactory.create(
        // Please provide this value from particular constant object.
        businessUnit = "your_business_unit",
        eventCategory = "your_event_category",
    )
}
```

4. Use your sender & helper function to send tracker

```
class YourTrackerImpl @Inject constructor(
    analyticManagerFactory: ContentAnalyticManager.Factory
) {
    
    private val analyticManager = analyticManagerFactory.create(
        /** Please provide this value from particular constant object. */
        businessUnit = "your_business_unit",
        eventCategory = "your_event_category",
    )
    
    /** Send normal event */
    fun clickUploadButton(author: ContentAccountUiModel, creationId: String) {
        analyticManager.sendClickContent(
            eventAction = "click - upload",
            eventLabel = analyticManager.concatLabelsWithAuthor(account.toAnalyticModel(), storyId),
            mainAppTrackerId = "47815",
            sellerAppTrackerId = "47933"
        )
    }
    
    /** Send open screen event */
    fun openScreenReviewTab() {
        analyticManager.sendOpenScreen(
            screenName = "/user profile - review tab",
            mainAppTrackerId = "44104",
        )
    }
    
    /** Send Enhance Ecommerce event */
    fun clickReviewProductInfo(
        userId: String,
        feedbackId: String,
        isSelf: Boolean,
        productReview: UserReviewUiModel.Product,
    ) {
        analyticManager.sendEEProduct(
            event = Event.selectContent,
            eventAction = "click - product on review",
            eventLabel = analyticManager.concatLabels(feedbackId, userId, UserProfileAnalytics.Function.isSelfOrVisitor(isSelf), productReview.productID),
            itemList = "/user profile - review tab",
            products = listOf(
                ContentEnhanceEcommerce.Product(
                    itemId = productReview.productID,
                    itemName = productReview.productName,
                    itemBrand = "",
                    itemCategory = "",
                    itemVariant = productReview.productVariant.variantName,
                    price = "",
                    index = "",
                    customFields = emptyMap(),
                )
            ),
            mainAppTrackerId = "44110"
        )
    }
}
```

---

## FAQ

<!--start expand:Can module outside content reuse this module?-->
Yes you can if you want attach content analytic in another module (e.g. shop\_page, home, etc). But if it’s not related to content we recommend to not use this module 🙏
<!--end expand-->

