---
title: "Campaign Common"
labels:
- seller-app
- campaign
- seller
- feature-common
- android
---


| **Status** |  <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence)  |
| Product Manager | [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |
| Team | Minion Ken |
| Release date | 10 Aug 2022 / <!--start status:GREY-->SA-2.118<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->  |
| Module Location | `features/merchant/campaign_common` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:10 Aug 2022 (SA-2.118)-->
- [Module is created](https://github.com/tokopedia/android-tokopedia-core/pull/27582) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
- [New: Added collections of extension function](/wiki/spaces/PA/pages/2084537866/Extension+Functions) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:15 Aug 2022-->
- [New components created: SingleSelectionBottomsheet](/wiki/spaces/PA/pages/2084472329/Single+Selection+Bottomsheet) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:16 Aug 2022-->
- [New components created: MultipleSelectionBottomsheet](/wiki/spaces/PA/pages/2084537891/Multiple+Selection+Bottomsheet) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:18 Aug 2022-->
- [New components created: CompositeAdapter](/wiki/spaces/PA/pages/2107149990/Composite+Adapter) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:27 Aug 2022-->
- [New components created: Paging - with Delegation](/wiki/spaces/PA/pages/2109965072/Pagination+-+with+Kotlin+Delegation) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:22 Sept 2022-->
- [New components created: Reusable Pages](/wiki/spaces/PA/pages/2108919288/Reusable+Pages) [CALVYN JULIAN](https://tokopedia.atlassian.net/wiki/people/5d1adf49cdf26a0d349c76ec?ref=confluence)
<!--end expand-->

<!--start expand:28 Sep 2022-->
- [New custom component created: IneligibleAccessBottomsheet](/wiki/spaces/PA/pages/2084570788/Ineligible+Access+Warning+BottomSheet) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

<!--start expand:03 Oct 2022-->
- [New custom view created: IneligibleAccessView](/wiki/spaces/PA/pages/2084570788/Ineligible+Access+Warning+BottomSheet) [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence)
<!--end expand-->

## Overview

Campaign common is a module. It contains collections of reusable UI components, widgets, reusable pages, and handy extension function that commonly used on campaign tribe features.

## Background

Based on campaign-tribe product metrics, they found that most of the sellers outside Jabodetabek is using Seller App in managing and creating their shop campaign. Unfortunately, compared to Desktop site, there are only couple of this features exist on Seller App. So, since the beginning of 2021, products team are working to do some parity to bring those features to be also available on Seller App. With this background as the context, in order to keep up with these new feature request, we, as the engineers invented the campaign common module to decrease code duplication and to speed up development time for any new features.

## How-to

To use any widgets, ext function that resides in this module, you can add campaign common module as your module dependency:



```
   implementation projectOrAar(rootProject.ext.features.campaignCommon)
```

## What’s inside this module?

To find out if any content of this module can help you, please checkout this following pages that correlates with your needs:

- [Extension Functions](/wiki/spaces/PA/pages/2084537866/Extension+Functions)
- [Reusable Components](/wiki/spaces/PA/pages/2084734301/Reusable+Components)
- [Composite Adapter](/wiki/spaces/PA/pages/2107149990/Composite+Adapter)
- [Reusable Pages](/wiki/spaces/PA/pages/2108919288/Reusable+Pages)
- [Pagination - with Kotlin Delegation](/wiki/spaces/PA/pages/2109965072/Pagination+-+with+Kotlin+Delegation)



---

## Who is this for?

This module mostly used by Minion Ken campaign-team engineers. But, it’s open for all Tokopedia Android Engineers. You can also use this module if you have some use case that exist within this module. We are open for any new contributor.

## Where is it being used?

Currently it is already used by these new features:

1. Flash Sale Tokopedia
2. Merchant Voucher Creation

## FAQ

###### *Any common questions that you couldn’t put in those sections above, you can put them in this FAQ section.*

<!--start expand:How can I contribute?-->
Have some idea in mind to contribute? Cool. Please reach out to one of the contributors on Slack for more details
<!--end expand-->

## Contributor

- [Fajar Chaeril Azhar](/wiki/display/~618873e3c23a4f006969b0de) (160 days ago)
- [CALVYN JULIAN](/wiki/display/~5d1adf49cdf26a0d349c76ec) (226 days ago)
