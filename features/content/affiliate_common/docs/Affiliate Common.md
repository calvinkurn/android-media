---
title: "Affiliate Common"
---


| **Status** | <!--start status:RED-->DEPRECATED<!--end status--> |
| --- | --- |
| **Contributors** |  [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence)  |
| Product Manager |  - |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 12 Mar 2018 / <!--start status:GREY-->MA-3.70<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | - |
| Module Location | `features.affiliateCommon` | `features/content/affiliate_common` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:March 31, 2023 (SA-2.144, MA-3.214)-->
[AN-48496](https://tokopedia.atlassian.net/browse/AN-48496)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Jan 6, 2023 (MA-3.203)-->
[AN-44431](https://tokopedia.atlassian.net/browse/AN-44431)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:Oct 14, 2022 (MA-3.196)-->
[AN-42096](https://tokopedia.atlassian.net/browse/AN-42096)
 -
 Getting issue details...

STATUS




  


 [AN-41879](https://tokopedia.atlassian.net/browse/AN-41879)
 -
 Getting issue details...

STATUS
<!--end expand-->

## Overview

The affiliate common module’s purpose is to support the affiliate module, meanwhile, the [affiliate](/wiki/spaces/PA/pages/2057865055/Affiliate) module is rewritten into a new module, and now this module is sad and lonely 

😢 .

Now, we are still in the progress of removing the unused classes, or whatever that unused in this module.

### Background

In order to support Tokopedia’s main metrics which are Paid Order and Buyer Acquisition, a Social-Affiliate project is built.

The idea is to enable everyone to promote our Sellers’ products through all their social channels (messaging platforms, social media, profile page, feed, or even word-of-mouth). Which in this case we call them, Affiliate (source: <https://github.com/tokopedia/android-tokopedia-core/pull/3735>).

### Project Description

“ByMe“ is what the feature was called before, which means users can see, click and buy the products shared

<img src="https://docs-android.tokopedia.net/images/docs/res/affiliate_common-content.png" alt="" />

by me. The remaining classes there, are classes for tracking products which should count as a commission if any user buys the product.

To be honest, when I wrote this document I was still not sure whether it could still be used or not. And right now, we're focusing on other things that we think are more urgent than figuring out what the purpose of this module is.

One thing is for sure, we are depreciating this module.

And, I recommend moving anything that's still in use and deleting what's not.

## Tech Stack

### Healthiness

- Kotlin Count 

**84.62%** (based on [Tokopedia App Statistics](https://docs.google.com/spreadsheets/d/1aJGHic-YMP2eEtZoT5eMqJK5qK4rRdbtYMPkv0yGlas/edit#gid=0) version MA-3.204)

### Dependent Modules



| **Module** | **Class** |
| --- | --- |
| merchant/feed\_shop | - import com.tokopedia.affiliatecommon.data.network.TOPADS\_BASE\_URL<br/>- import com.tokopedia.affiliatecommon.data.network.TopAdsApi<br/>- import com.tokopedia.affiliatecommon.BROADCAST\_SUBMIT\_POST<br/>- import com.tokopedia.affiliatecommon.SUBMIT\_POST\_SUCCESS<br/> |
| merchant/product\_detail | - import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse<br/>- import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase<br/> |
| merchant/shop\_page |  |
| content/create\_post | - import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics<br/>- import com.tokopedia.affiliatecommon.\*<br/> |
| content/create\_post\_common | - import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics<br/>- import com.tokopedia.affiliatecommon.\*<br/> |
| content/explore | - import com.tokopedia.affiliatecommon.data.network.TopAdsApi<br/>- import com.tokopedia.affiliatecommon.data.util.AffiliatePreference<br/>- import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase<br/> |
| content/feed | - import com.tokopedia.affiliatecommon.data.network.TopAdsApi<br/>- import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase<br/>- import com.tokopedia.affiliatecommon.data.util.AffiliatePreference<br/> |
| content/kol |  |

Note that the `affiliate_common` module can be removed from `merchant/shop_page` and `content/kol` modules.

### Unused Dependencies



```
implementation projectOrAar(rootProject.ext.features.resourcesCommon)
implementation projectOrAar(rootProject.ext.libraries.tkpddesign)
implementation rootProject.ext.unifyDependencies.principles
implementation rootProject.ext.unifyDependencies.components
implementation rootProject.ext.unifyDependencies.icons
implementation rootProject.ext.unifyDependencies.global_error
implementation rootProject.ext.supportLibDependencies.constraintLayout
implementation rootProject.ext.miscDependencies.roundCornerProgressBar
implementation rootProject.ext.networkDependencies.glide
implementation rootProject.ext.supportLibDependencies.kotlinSupportJdk7
```

👆 all of these are safe to be removed from `affiliate_common/build.gradle`

## Navigation

There is no activity, fragment, or view in this module. What's left is only the use case, preference, and tracker classes.

## How-to

Instead of using any of the classes inside this module, I recommend you just move the class you need to your module. And how to do that?

- Cmd + X
- Cmd + V

easy right? 😆 

and next, ask for our approval (we’ll automatically approve your PR 🫱🏻‍🫲🏼 ).



---

## Action Items

- Moving anything that's still in use and deleting what's not ([Pull Request: removal of unused classes and resources](https://github.com/tokopedia/android-tokopedia-core/pull/31439)) 

 [AN-48496](https://tokopedia.atlassian.net/browse/AN-48496)
 -
 Getting issue details...

STATUS
- Delete the whole module

## Useful Links

-

[data-colorid=ti7lxi9dsg]{color:#bf2600} html[data-color-mode=dark] [data-colorid=ti7lxi9dsg]{color:#ff6640}[data-colorid=kj4pfl1tot]{color:#bf2600} html[data-color-mode=dark] [data-colorid=kj4pfl1tot]{color:#ff6640}