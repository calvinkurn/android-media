---
title: "Flash Sale Toko"
labels:
- hyperlocal
- merchant
- seller-app
- flash-sale
- campaign
- toko
- product
- promo
- flash-sale-toko
- seller
---


| **Status** |  <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors |  [Fajar Chaeril Azhar](https://tokopedia.atlassian.net/wiki/people/618873e3c23a4f006969b0de?ref=confluence) [Faisal Ramdani](https://tokopedia.atlassian.net/wiki/people/5def016f73b0bd0e4d82fa6e?ref=confluence) [Muhammad Fiqri Muthohar](https://tokopedia.atlassian.net/wiki/people/6163fa1499b4b8006afdf234?ref=confluence) [Yuda Eka Putra](https://tokopedia.atlassian.net/wiki/people/6253583e9770e600716830da?ref=confluence)  |
| Product Manager | [Danis Mandasari](https://tokopedia.atlassian.net/wiki/people/61513d2b9cdb930072065a8b?ref=confluence)  |
| Team | [Minion Ken](https://tokopedia.atlassian.net/people/team/0ac7bdd0-19b2-4196-8711-b1a0a4b07178)  |
| Release date | TBC release date/ <!--start status:GREY-->SA 2.119<!--end status-->  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD](https://www.google.com/url?q=https://docs.google.com/document/d/1Gode0-PO6bCNAL7Y61oNDRfuKWZWcUubb5dRTNp7igg/edit&sa=D&source=calendar&ust=1651315144986473&usg=AOvVaw0TG9kfm_obr7EeHNnQMty3 ) |
| Module Location | `features.merchant` | `features/merchant/seller_shop_flash_sale` |

## Table of Contents

<!--toc-->



---

## Overview

### Background

Accelerating hyperlocal expansion through localized campaign experiences and seller app parity

Throughout 2021, several “hyperlocal campaigns” have been carried out to provide local buyers with more selections of campaign products (e.g. Kejar Diskon for New Buyer, local Kejar Diskon, etc.), considering 73% of campaign products are still coming from Jabodetabek. The local campaign supply is mainly coming from RGX. Within 10 months, the number of campaign products in 14 hyperlocal cities have increased by 3x from 8k in Jan’21 to 27k in Oct’21 and orders have increased by 7x from 50k orders to 327k orders (~22% of overall KD orders). Seeing good traction from local sellers and buyers, in H1 we will accelerate the availability of good campaign products in hyperlocal areas by: 

Adding promotion tools in Seller App, starting with Slash Price and Flash Sale Toko in H1, considering 75% of active sellers outside Jabodetabek use Android with avg. time spent of 20 mins vs 13 mins on the Desktop. With this, we aim to increase participation from local sellers and available campaign products by 3x. To ensure these campaign products can be easily discovered by local buyers, we will combine these seller-side improvements with hyperlocal initiative from the front funnel to target campaign page entry points ‘based on the buyers’ location, boost campaign products from nearby locations and direct buyers to location/ warehouse with the lowest Total Cost of Ownership (“TCO”).

### Project Description

***Flash Sale Toko*** is a feature, where seller can see, create and edit their Flash Sale by seller campaign.   
*Flash Sale Toko* has a few page that consist of:

1. **Introduction Page**  
In this page seller can see the benefits they get when creating Flash Sale by seller campaign and also CTA to go to pusat edukasi or create campaign right away
2. **Campaign list**  
In here, seller can see their active and history campaign that is divided by different tabs. All the campaign data is show in a list of card containing a glance of the respective campaign information
3. **Campaign detail**  
In here, seller can see a more detailed information of the campaign they choose from the campaign list. Also a capability to download the campaign assets, share campaign to social media and Add/Edit/Delete product
4. **Create Campaign**  
In order to create campaign, seller have to gone through a few steps and fill the information needed to create the campaign, the steps are:  
a. Campaign Info   
b. Product Submission  
c. Manage Product  
d. Campaign Rule & Save Campaign



---

## Tech Stack

- Dagger
- Livedata
- Flow
- mockk
- GQL
- REST
- MVVM
- KOTLIN



---

## Flow Diagram

![](res/Screen%20Shot%202022-12-15%20at%2016.52.49.png)



---

## Navigation

There are two ways to navigate to the ***Flash Sale Toko*** feature, here’s the step by step on how to access it

1. **Applink**  
The first way is by using applink, ***Flash Sale Toko*** have this 2 applinks, that can be used to navigate you directly to the flash sale list page, since the flash sale listpage is having 2 tabs, here are the applink to access each tabs :


	1. *sellerapp://shop-flash-sale/active*
	2. *sellerapp://shop-flash-sale/history*
2. **Menu**

![](res/Screenshot_20230105-122221_Tokopedia%20Seller-20230105-052714.jpg)

![](res/Screenshot_20230105-122246_Tokopedia%20Seller-20230105-052820.jpg)

![](res/Screenshot_20230105-122255_Tokopedia%20Seller-20230105-052906.jpg)

![](res/Screenshot_20230105-122305_Tokopedia%20Seller-20230105-052520.jpg)



---

## How-to

- Open the page via Applink → use [RouteManager](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1419983437/Applink+Library#How-to-use-library) class. You can also test the AppLink via the [Developer Options](#) page.
- Compile this module → just choose the `features:merchant:seller_shop_flash_sale` module from [Switch to Testapp](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1428784251/Testapp+Build+Plugin) menu (please install the Switch to Testapp plugin first, find the plugin on [Testapp Build & Plugin](/wiki/spaces/PA/pages/1428784251)).



---

## Action Items

- Improve & maintain UT Coverage to above 96%



---

## Useful Links

- [Seller App survey](https://docs.google.com/presentation/d/1yiTbKR_mnw7tG0xw1sdy_fgE19WrF79CRZOFYUfE2ao/edit#slide=id.g860c8fbf0b_0_164)
- [Figma](https://www.figma.com/file/9dV6755EbpdIfHRzi5X3zG/FST-on-Seller-App?node-id=25%3A13528 )
- [Thanos](https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3096)
- [PRD](https://www.google.com/url?q=https://docs.google.com/document/d/1Gode0-PO6bCNAL7Y61oNDRfuKWZWcUubb5dRTNp7igg/edit&sa=D&source=calendar&ust=1651315144986473&usg=AOvVaw0TG9kfm_obr7EeHNnQMty3 )
- [RouteManager](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1419983437/Applink+Library#How-to-use-library)
- [Switch to Testapp](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1428784251/Testapp+Build+Plugin)
- [Testapp Build & Plugin](/wiki/spaces/PA/pages/1428784251)



---

