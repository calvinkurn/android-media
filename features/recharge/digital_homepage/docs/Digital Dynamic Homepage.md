---
title: Digital Dynamic Homepage
labels:
---
<!--left header table-->
| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Team | [Minion Mark](https://tokopedia.atlassian.net/people/team/54372146-8afa-46e4-8de3-783c53a0cc3b) |
| Module PIC | Irfan Muhlishin (temporary) |
| Product Manager | Syarief Husin  |
| Module Type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/recharge/digital_homepage` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:13 Dec 22 (MA-3.202)-->
In this release, we add 2 new templates `MYBILLS_ENTRYPOINT` & `BILL_WIDGET` as part of Bill Management project.

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/30695>
<!--end expand-->

<!--start expand:25 Jul 22 (MA-3.184)-->
In this release, we add new template `OFFERING_WIDGET`

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/27369>
<!--end expand-->

## Overview

Digital Homepage is a page containing a list of digital products. There are several types of templates (view type) which dynamically rendered based on BE response. Digital Homepage has several types determined by the platform id, which are Recharge products, Travel products, Kartu Prakerja (KPK) products, and Streaming products.

## Screenshot

**Page overview**



| Recharge Subhomepage | Travel Subhomepage | Streaming Subhomepage |
| --- | --- | --- |
|  |  |  |

## Tech Design

### How Dynamic sub-homepage works?

![](res/Untitled Diagram.png)Visit diagram here: <https://drive.google.com/file/d/148hmVKe7eCGqurSMEef6XyIcgM-gBFok/view?usp=sharing> 

### **Notes on some cases/flow**

- A RechargeHomepageSection will be removed from the page if apps failed to load the specific recharge section data.
- If BE returns empty ticker data, then it will not be displayed on the page.
- For `SECTION_COUNTDOWN_SINGLE_BANNER` component, the widget will automatically disappear if the countdown time is expired
- When the user pull to refresh this page, apps will re-fetch skeleton data from the cloud

### List of Dynamic sub-homepage’s component

![](res/image-20210112-045934.png)Tech Design diagram: <https://drive.google.com/file/d/1IYPgRqP5taKQr6HUVgvZ512dtZ8iCSAF/view?usp=sharing> 

### Details of sub-homepage’s components

<https://drive.google.com/file/d/1MsJbNzwxeMu4he80sFQddGajgRDLOqK9/view?usp=sharing> 



| **No** | **TEMPLATE** | **ViewHolder** | **Screenshot** |
| --- | --- | --- | --- |
| 1 | `TOP_BANNER` | `RechargeHomepageBannerViewHolder` |  |
| 2 | `TOP_BANNER_EMPTY` | `RechargeHomepageBannerEmptyViewHolder` |  |
| 3 | `TOP_ICONS` | `RechargeHomepageFavoriteViewHolder` |  |
| 4 | `DYNAMIC_ICONS` | `RechargeHomepageCategoryViewHolder` |  |
| 5 | `DUAL_ICONS` | `RechargeHomepageDualIconsViewHolder` |  |
| 6 | `URGENCY_WIDGETS` | `ReminderWidgetViewHolder` |  |
| 7 | `VIDEO_HIGHLIGHT` | `RechargeHomepageVideoHighlightViewHolder` |  |
| 8 | `SINGLE_BANNER` | `RechargeHomepageSingleBannerViewHolder` |  |
| 9 | `COUNTDOWN_SINGLE_BANNER` | `RechargeHomepageSingleBannerViewHolder`notes: - the widget will automatically disappear if the countdown time is expired<br/> |  |
| 10 | `DUAL_BANNERS` | `RechargeHomepageDualBannerViewHolder` |  |
| 11 | `LEGO_BANNERS` | `DynamicLegoBannerViewHolder` |  |
| 12 | `PRODUCT_CARD_ROW` | `RechargeHomepageProductCardsViewHolder` | New UI in v.3.131 above: |
| 13 | `PRODUCT_CARD_ROW_1X1` | `RechargeHomepageProductCardsViewHolder` | using the same view holder with `PRODUCT_CARD_ROW` |
| 14 | `COUNTDOWN_PRODUCT_BANNER` | `RechargeHomepageProductBannerViewHolder` |  |
| 15 | `PRODUCT_CARD_CUSTOM_BANNER` | `RechargeHomepageProductCardCustomBannerViewHolder` |  |
| 16 | `MINI_CAROUSELL` | `RechargeHomepageCarousellViewHolder` |  |
| 17 | `SWIPE_BANNER` | `RechargeHomepageSwipeBannerViewHolder` |  |
| 18 | `PRODUCT_CARD_DGU` | `RechargeHomepageProductCardUnifyViewHolder` |  |
| 19 | `3_ICONS` | `RechargeHomepageThreeIconsViewHolder` |  |
| 20 | `PRODUCT_CARD_CUSTOM_BANNER_V2` | `RechargeHomepageProductCardCustomBannerV2ViewHolder` |  |
| 21 | `2X2_BANNER` | `RechargeHomepageRecommendationBannerViewHolder` |  |
| 22 | `PRODUCT_CARD_CUSTOM_LAST_ITEM` | `RechargeHomepageProductCardCustomLastItemViewHolder` |  |
| 23 | `OFFERING_WIDGET` | `RechargeHomepageOfferingWidgetViewHolder` |  |
| 24 | `BILL_WIDGET` | `RechargeHomepageMyBillsWidgetViewHolder` |  |
| 25 | `MYBILLS_ENTRYPOINT` | `RechargeHomepageMyBillsEntryPointWidgetViewHolder` |  |
| - | For loading purposes | `RechargeHomepageLoadingViewHolder` |  |

### How sub-homepage’s search bar works?

- Happy Flow

![](res/image-20210520-083934.png)## API List



| **Query / Mutation / API** | **Usage** | **Triggered By** | **API Docs** |
| --- | --- | --- | --- |
| `rechargeGetDynamicPageSkeleton` | Get the skeleton (list of widget) of Digital Homepage | - Open page<br/>- Pull to refresh<br/> | [[BTDC] Dynamic Page](/wiki/spaces/BT/pages/813827413)  |
| `rechargeGetDynamicPage` | Get the detail of the widget | Bind widget’s view holder for the first time  |
| `rechargePostDynamicPageAction` | Notify BE when user tap close button on Reminder widget (`URGENCY_WIDGETS`) & Banner widget (`COUNTDOWN_PRODUCT_BANNER`) | Tap close button |
| `rechargeTicker` | Get available ticker information | - Open page<br/>- Pull to refresh<br/> | [[BTDC] Recharge Ticker](/wiki/spaces/BT/pages/1282442521)  |

## Applinks

Dynamic Subhomepage applink: 

`tokopedia://recharge/home?platform_id={id}&personalize={is_personalize}`



| **Product** | **Applink** |
| --- | --- |
| Recharge Dynamic Subhomepage (Personalized) | tokopedia://recharge/home?platform\_id=31 |
| Recharge Dynamic Subhomepage | tokopedia://recharge/home?platform\_id=31&personalize=false |
| Travel Dynamic Subhomepage  | tokopedia://recharge/home?platform\_id=34&personalize=false |
| Kartu Prakerja Homepage  | tokopedia://recharge/home?platform\_id=35&personalize=false |
| Streaming Subhomepage | tokopedia://recharge/home?platform\_id=36&personalize=false |

Please visit [[BTDC] Platform ID List](/wiki/spaces/BT/pages/546013484) for more info about Platform ID 

## Useful Links

- [BE Documentation](/wiki/spaces/BT/pages/813827413)
- [Platform ID Documentation](/wiki/spaces/BT/pages/546013484)

