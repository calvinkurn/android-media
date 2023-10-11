<!--left header table-->
| **Status** |  <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributor | [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) [Said Faisal](https://tokopedia.atlassian.net/wiki/people/5e25eee0ee264b0e745862c3?ref=confluence) [Firmanda Mulyawan Nugroho](https://tokopedia.atlassian.net/wiki/people/5d91c148fdfa560dcc3a040f?ref=confluence) [Reza Gama Hidayat](https://tokopedia.atlassian.net/wiki/people/5def15952702bc0ec7e775c5?ref=confluence) [Darian Thedy](https://tokopedia.atlassian.net/wiki/people/5c94aa568c3aae2d15117504?ref=confluence) [Misael Jonathan](https://tokopedia.atlassian.net/wiki/people/60051d42e64c95006fbaad73?ref=confluence) [Steven Fredian Andy Putra](https://tokopedia.atlassian.net/wiki/people/557058:20782bf2-2a29-413c-b75c-ce30c92cad9e?ref=confluence)  |
| Product Manager | [Deo Nathaniel](https://tokopedia.atlassian.net/wiki/people/5c6be6f577edd55f716a2258?ref=confluence) [Joshua Edbert Tirtana](https://tokopedia.atlassian.net/wiki/people/60f7ac85f026ab007029a6bf?ref=confluence) [Ryan Mico](https://tokopedia.atlassian.net/wiki/people/5c6bedd8cff26405c30ad1b1?ref=confluence) [Jumadila Mustika](https://tokopedia.atlassian.net/wiki/people/61c037f6a54af90069a11858?ref=confluence)  |
| QA & TE | [Asti Lestari](https://tokopedia.atlassian.net/wiki/people/5a6ae82288193b25ef8e6b89?ref=confluence) [Nanda Namira Tiarninda](https://tokopedia.atlassian.net/wiki/people/5c370886c9c9fc6f5988cfdd?ref=confluence) [Sita Nirmala Citra Christiani](https://tokopedia.atlassian.net/wiki/people/62b94477c9f2df7b6089e539?ref=confluence) [Arlisa Liana](https://tokopedia.atlassian.net/wiki/people/620b14cd59709300698d987c?ref=confluence) [Muhammad Rafi](https://tokopedia.atlassian.net/wiki/people/620b0a5307f51e0069426cd4?ref=confluence) [Vincent .](https://tokopedia.atlassian.net/wiki/people/6163f9fe07ac3c0068657be0?ref=confluence) [Dewi Mulki](https://tokopedia.atlassian.net/wiki/people/6283429ccc1d15006fa9da3f?ref=confluence)  |
| Release Date | 14 Jul 2021  |
| Team | Minion Solo |
| Module Type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | <https://docs.google.com/document/d/1pWsEA_85v9vZWmfxXo5DC0xsb7QWDu_cTNjMMQpnFsk/edit> |
| Module Location |  `features/tokopedianow` |

<!--toc-->

## Release Notes

<!--start expand:MA-3.206-->
###### Revamp Category Menu

###### PR:<https://github.com/tokopedia/android-tokopedia-core/pull/31231>
<!--end expand-->

<!--start expand:MA-3.203-->
###### Create Now Product Card

###### PR:<https://github.com/tokopedia/android-tokopedia-core/pull/29216>
<!--end expand-->

<!--start expand:MA-3.202-->
###### Create Real Time Recommendation

###### PR:<https://github.com/tokopedia/android-tokopedia-core/pull/30261>
<!--end expand-->

![image](res/now_logo.png)

## Overview

The pandemic has sped up the digitalization of grocery shopping worldwide. In Tokopedia, we have also witnessed order & MUB in the FMCG categories grow by 3X, while the overall order frequency / MUB increased 15% compared to the same period last year. Nevertheless, online players account for only 6% of the total grocery market in 2020 due to the high cost of last-mile-delivery as well as the lack of fast and convenient grocery-like shopping experience. With 90M+ customers in our platform, we feel that this is the right time to invest in the tech and infrastructure of delivering essential items. We will focus on giving our customers premium selection with competitive prices, guaranteed <2h delivery and most importantly the best buying experience. More business context can be found here, this document is describing how we win the latter.

Tokopedia’s current purchase experience is not fit to buying essential items. Unlike our online competitors, (a) we do not offer a 24 hour guaranteed instant delivery to fulfill urgent and impulse needs. (b) It’s difficult to discover a shop that sells a range of staples and non-staples products with the right price range. Even if there is, (c) the selections are not grouped properly which makes browsing challenging, and (d) no quick and easy way to add these products to cart while customers browse. Lastly, (e) there’s no experience that supports scheduled re-ordering which is crucial to the continuity of the business since buying essentials is habitual to the majority of people.

### Project Description

Our vision is to create a one stop shop to all essential necessities that can be delivered within one shipment in less than 2 hours. To achieve this, we will :

1. Offer guaranteed and free 2 hour delivery as a new USP Provide a service that can pick, pack and deliver to the customer’s doorstep within 2 hours from the moment they completed payment. Customer’s can GPS track the order to time the arrival of the package. There’ll be an identifier throughout the buyer journey to distinguish selections that are eligible for this service.
2. Introduce Tokopedia whitelabeled selection model. For the first time ever in our marketplace ecosystem, we'll introduce product selections that are sold and fulfilled by Tokopedia. Such that customers don’t have to worry about the seller's credibility, fulfillment SLA or returns. Since technically Tokopedia is not a shop, we'll limit certain unnecessary capabilities that are related to it, for example  
following the store, checking the store’s statistics or even browsing the shop page. We'll replace all of the required functionality within the NOW! page and the overall NOW! journey which will be explained more on (3).
3. Optimize the discovery and browsing journey for essential items. When shopping in NOW!, we want customers to always start from the NOW! page to provide the best grocery shopping experience. The NOW! page is location aware, and will only present selections that are available in the nearest Tokopedia Hub. For customers that are within Hub’s the service area, they can access the NOW! page from an always on icon at the homepage. On search, we will limit the appearance of NOW! products on the search result pages to avoid head to head competition with other sellers, however there will be a visible entry point to the NOW! page for each related search keywords. Once in the NOW! homepage, we’ll give them the option to browse within the product categories to replicate the offline supermarket aisles experience. They will also have the option to browse through different sections such as their past purchases, discounted items, and also thematic sections (i.e Ramadan, recipes, recommendations, etc). In addition, customers will be able to do local searches within the NOW! environment which supports product name, categories, brands, or even recipes. All NOW! browsing experience will be accommodated through NOW! homepage and sub-pages that will replace the unavailability of the Shop Page.
4. Enable quick ATC and Checkout within the browsing experience. Customers most likely do not need extensive information about essential items. There is less necessity for them to go to the PDP and read about the review, description or chat. Hence, we'll give our customers an alternative to doa direct add to cart on the NOW! pages, along with choosing quantity and variants, all while they browse. The PDP itself will be simpler, removing unnecessary information such as shop performance, diskusi, and emphasizing on category specific information such as nutrient facts and expiry dates. For checkout, we’ll also give an option to do a One Click Checkout from the NOW! pages, to avoid all the hassle after they finish browsing. However, they can still go through the regular cart-checkout flow if they choose to do so. The Cart page will have a special section for NOW! items that is pinned on top among other products. Furthermore, customers are able to choose the two hour delivery service, COD payment method as well as the return policy on the checkout page.
5. Allow easy reorder and scheduling for habitual purchases. Essential products such as food and groceries can only last for a week max, therefore an effortless re-ordering system is key to ensure buyer repeat purchases. First, we’ll always show their past purchased SKUs throughout the NOW! environment that are contextual on home, category or search. Second, customers can also re-order the exact same list with one click from multiple points such as the NOW! homepage or the Order History list. Lastly, customers can create a scheduled reminder for an order, for those who like to plan ahead, this will be our foundation to create a subscription service later on.

## Useful Links

### Figma

- Pilot Release (July): <https://www.figma.com/file/ywlnYgKxnz7AYYtPDhTs9T/UX%2FUI---TokoNOW!-%5BPilot-Release---July%5D?node-id=3441%3A13051>
- Grand Launch (August): <https://www.figma.com/file/BdMdmdfURvDmlHpx8r6pe8/UX%2FUI---Tokopedia-NOW!-%5BGrand-Launch---August%5D?node-id=1395%3A8825>
- Grand Launch (September) : <https://www.figma.com/file/vxDHPPyinsXDoqckLdLscY/UX%2FUI---Tokopedia-NOW!-%5BSpillover---September%5D>

### Thanos

- Repurchase Widget: <https://mynakama.tokopedia.com/datatracker/product/requestdetail/1919>
- Open Screen OOC: <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2388>
- Tokopedia NOW! Home page: <https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/1393>
- Sharing Education: <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2147>
- Product Recom OOC: <https://mynakama.tokopedia.com/datatracker/product/requestdetail/1910>
- Tokopedia NOW! Repurchase page: <https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/2207>
- Open Screen Home page: <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2099>
- OOC Home page : <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2112>
- Chat Attachment Bottom Sheet : <https://mynakama.tokopedia.com/datatracker/product/requestdetail/1995>
- Product Recom Home : <https://mynakama.tokopedia.com/datatracker/product/requestdetail/1712>
- Tokopedia NOW! Category page: <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2100>
- OOC Category page : <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2216>
- OOC Search page : <https://mynakama.tokopedia.com/datatracker/requestdetail/view/2219>

### Pages

- [Home](/wiki/spaces/PA/pages/1728218506/Home)
- [Repurchase](/wiki/spaces/PA/pages/1845723195/Repurchase)
- [Search & Category](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/1607402386/NOW+Search+Category)
- [Common Widgets](/wiki/spaces/PA/pages/2129992602/Common+Widgets)

