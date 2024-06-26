---
Digital Dynamic Homepage
---
<!--left header table-->
| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Team | [Minion Mark](https://tokopedia.atlassian.net/people/team/54372146-8afa-46e4-8de3-783c53a0cc3b) |
| Module PIC | Irfan Muhlishin (temporary) |
| Product Manager | [Fardhan Amarullah](https://tokopedia.atlassian.net/wiki/people/5e13b89c0242870e996f0490?ref=confluence)  |
| Module Type | <!--start status:YELLOW-->FEATURE<!--end status--> |
| Module Location | `features/recharge/digital_homepage` |

## Table of Contents

<!--toc-->

## Release Notes

<!--left header table-->
| **Date**  | **App Version** | Notes                                                                                        | PR                                                               |
|-----------|-----------------|----------------------------------------------------------------------------------------------|------------------------------------------------------------------|
| 26 May 23 | MA-3.222        | Add 1 new templates `3_BUTTONS_MYBILLS_ENTRYPOINT`                                           | <https://github.com/tokopedia/android-tokopedia-core/pull/33081> |
| 13 Dec 22 | MA-3.202        | Add 2 new templates `MYBILLS_ENTRYPOINT` & `BILL_WIDGET` as part of Bill Management project. | <https://github.com/tokopedia/android-tokopedia-core/pull/30695> |
| 25 Jul 22 | MA-3.184        | Add 1 new template `OFFERING_WIDGET`                                                         |  <https://github.com/tokopedia/android-tokopedia-core/pull/27369> |

## Overview

Digital Homepage is a page containing a list of digital products. There are several types of templates (view type) which dynamically rendered based on BE response. Digital Homepage has several types determined by the platform id, which are Recharge products, Travel products, Kartu Prakerja (KPK) products, and Streaming products.

## Screenshot

**Page overview**



| Recharge Subhomepage | Travel Subhomepage | Streaming Subhomepage |
| --- | --- | --- |
| ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/Screenshot_20230111_085119.png)<br/> | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/Screenshot_20230111_085032.png)<br/> | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/Screenshot_20230111_085507.png)<br/> |

## Tech Design

### How Dynamic sub-homepage works?

![](https://docs-android.tokopedia.net/images/docs/digital_homepage/Untitled Diagram.png)

Visit diagram here: <https://drive.google.com/file/d/148hmVKe7eCGqurSMEef6XyIcgM-gBFok/view?usp=sharing> 

### **Notes on some cases/flow**

- A RechargeHomepageSection will be removed from the page if apps failed to load the specific recharge section data.
- If BE returns empty ticker data, then it will not be displayed on the page.
- For `SECTION_COUNTDOWN_SINGLE_BANNER` component, the widget will automatically disappear if the countdown time is expired
- When the user pull to refresh this page, apps will re-fetch skeleton data from the cloud

### List of Dynamic sub-homepage’s component

![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210112-045934.png)

Tech Design diagram: <https://drive.google.com/file/d/1IYPgRqP5taKQr6HUVgvZ512dtZ8iCSAF/view?usp=sharing> 

### Details of sub-homepage’s components

<https://drive.google.com/file/d/1MsJbNzwxeMu4he80sFQddGajgRDLOqK9/view?usp=sharing> 



| **No** | **TEMPLATE** | **ViewHolder** | **Screenshot** |
| --- | --- | --- | --- |
| 1 | `TOP_BANNER` | `RechargeHomepageBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-100242.png)<br/> |
| 2 | `TOP_BANNER_EMPTY` | `RechargeHomepageBannerEmptyViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-100950.png)<br/> |
| 3 | `TOP_ICONS` | `RechargeHomepageFavoriteViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-093244.png)<br/> |
| 4 | `DYNAMIC_ICONS` | `RechargeHomepageCategoryViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-093256.png)<br/> |
| 5 | `DUAL_ICONS` | `RechargeHomepageDualIconsViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-093748.png)<br/> |
| 6 | `URGENCY_WIDGETS` | `ReminderWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-114955.png)<br/> |
| 7 | `VIDEO_HIGHLIGHT` | `RechargeHomepageVideoHighlightViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-102120.png)<br/> |
| 8 | `SINGLE_BANNER` | `RechargeHomepageSingleBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-103143.png)<br/> |
| 9 | `COUNTDOWN_SINGLE_BANNER` | `RechargeHomepageSingleBannerViewHolder`notes: - the widget will automatically disappear if the countdown time is expired<br/> | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-103143.png)<br/> |
| 10 | `DUAL_BANNERS` | `RechargeHomepageDualBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-103507.png)<br/> |
| 11 | `LEGO_BANNERS` | `DynamicLegoBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-115450.png)<br/> |
| 12 | `PRODUCT_CARD_ROW` | `RechargeHomepageProductCardsViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-104103.png)<br/>New UI in v.3.131 above:![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210623-110014.png)<br/> |
| 13 | `PRODUCT_CARD_ROW_1X1` | `RechargeHomepageProductCardsViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210623-110034.png)<br/>using the same view holder with `PRODUCT_CARD_ROW` |
| 14 | `COUNTDOWN_PRODUCT_BANNER` | `RechargeHomepageProductBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-105628.png)<br/> |
| 15 | `PRODUCT_CARD_CUSTOM_BANNER` | `RechargeHomepageProductCardCustomBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-110848.png)<br/> |
| 16 | `MINI_CAROUSELL` | `RechargeHomepageCarousellViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-115830.png)<br/> |
| 17 | `SWIPE_BANNER` | `RechargeHomepageSwipeBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230217at184109.png)<br/> |
| 18 | `PRODUCT_CARD_DGU` | `RechargeHomepageProductCardUnifyViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230217at184630.png)<br/> |
| 19 | `3_ICONS` | `RechargeHomepageThreeIconsViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at195224.png)<br/> |
| 20 | `PRODUCT_CARD_CUSTOM_BANNER_V2` | `RechargeHomepageProductCardCustomBannerV2ViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at195147.png)<br/> |
| 21 | `2X2_BANNER` | `RechargeHomepageRecommendationBannerViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at195251.png)<br/> |
| 22 | `PRODUCT_CARD_CUSTOM_LAST_ITEM` | `RechargeHomepageProductCardCustomLastItemViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at195112.png)<br/> |
| 23 | `OFFERING_WIDGET` | `RechargeHomepageOfferingWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at194747.png)<br/> |
| 24 | `BILL_WIDGET` | `RechargeHomepageMyBillsWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at194700.png)<br/> |
| 25 | `MYBILLS_ENTRYPOINT` | `RechargeHomepageMyBillsEntryPointWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230215at194645.png)<br/> |
| 26 | `3_BUTTONS_MYBILLS_ENTRYPOINT` | `RechargeHomepageMyBillsTripleEntryPointWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShot20230522at125551.png)<br/> |
| 27 | `TODO_WIDGET`                   | `RechargeHomepageTodoWidgetViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/ScreenShotTodoWiget.png)<br/>   |  
| - | For loading purposes | `RechargeHomepageLoadingViewHolder` | ![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-115856.png)<br/> |

### How sub-homepage’s search bar works?

- Happy Flow

![](https://docs-android.tokopedia.net/images/docs/digital_homepage/image-20210520-083934.png)

## API List



| **Query / Mutation / API** | **Usage**                                                                                                                | **Triggered By**                             | **API Docs** |
| --- |--------------------------------------------------------------------------------------------------------------------------|----------------------------------------------| -- |
| `rechargeGetDynamicPageSkeleton` | Get the skeleton (list of widget) of Digital Homepage                                                                    | - Open page<br/>- Pull to refresh<br/>       | [[BTDC] Dynamic Page](/wiki/spaces/BT/pages/813827413) |
| `rechargeGetDynamicPage` | Get the detail of the widget, now we prioritize use section_name as parameter to get section data, instead of section id | Bind widget’s view holder for the first time |
| `rechargePostDynamicPageAction` | Notify BE when user tap close button on Reminder widget (`URGENCY_WIDGETS`) & Banner widget (`COUNTDOWN_PRODUCT_BANNER`) | Tap close button                             |
| `rechargeTicker` | Get available ticker information                                                                                         | - Open page<br/>- Pull to refresh<br/>       | [[BTDC] Recharge Ticker](/wiki/spaces/BT/pages/1282442521) |
| `digitalPersonalizationCloseWidget` | Send Close Action Todo Widget for WATF close process                                                                     | - Close Widget Todo Widget<br/>-             |   |

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

