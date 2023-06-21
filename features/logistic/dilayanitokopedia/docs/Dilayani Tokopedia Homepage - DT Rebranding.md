---
title: Dilayani Tokopedia Homepage - DT Rebranding
labels:
- android
- logistic
- dilayani-tokopedia-homepage
---

<!--left header table-->
| **Status** | <!--start status:PURPLE-->DEVELOPMENT<!--end status-->                                                                                                                                                                                                                                                     |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Contributors | [Irpan .](https://tokopedia.atlassian.net/wiki/people/6253578a3bf0f0007015669c?ref=confluence) [Fakhira Devina](https://tokopedia.atlassian.net/wiki/people/61077e53b704b40068e80a8e?ref=confluence) [Eka Desyantoro](https://tokopedia.atlassian.net/wiki/people/6283196bd9ddcc006e9c7a85?ref=confluence) |
| Product Manager | [Fadhil Huda](https://tokopedia.atlassian.net/wiki/people/61e501c284311c00690960c3?ref=confluence)                                                                                                                                                                                                         |
| Team | [Minion Bob](https://tokopedia.atlassian.net/people/team/2373d8a6-1afc-4f2a-aa7a-63855c273051)                                                                                                                                                                                                             |
| Release date | **TBD**                                                                                                                                                                                                                                                                                                    |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                         |
| Module Location | `features/logistic/dilayanitokopedia`                                                                                                                                                                                                                                                                      |

<!--toc-->

## Release Notes

<!--start expand:not release yet-->

<!--end expand-->

## **Overview**

### Background

Aim to further build on buyer’s awareness of the Dilayani Tokopedia service which offers them the Fast, Free and Reliable service.

### Project Description

With the introduction of a dedicated DT Homepage, we aim to improve our buyer’s navigation experience whilst also ensuring that the internal DT team can easily scale up the exposure given to DT products with the supply increase.

### UI Page

here is UI Dilayani Tokopedia homepage

![](res/Dilayani%20Tokopedia%20Homepage%202.png)

### Dilayani Tokopedia Widgets

#### Navigation Toolbar

using navigation toolbar from `tokopedia home/searchbar` to navigate to search, share, cart, and main menu. 

![](res/Screenshot%202023-02-23%20at%2014.07.07.png)

related doc attached in [Useful Links](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2039617794/Dilayani+Tokopedia+Homepage+-+DT+Rebranding#Useful-Links) 

**Choose Address**

Using localization choose address from `logistic/localizationchooseaddress`. 

![](res/Screenshot%202023-02-23%20at%2014.06.48.png)

related doc attached in [Useful Links](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2039617794/Dilayani+Tokopedia+Homepage+-+DT+Rebranding#Useful-Links) 

#### Dilayani Tokopedia Anchor Category Tab

Currently this widget part of page of Dilayani Tokopedia inside module `logistic/dilayanitokopedia`

![](res/Screenshot%202023-02-23%20at%2014.06.02.png)

#### Sliding Banner

using `BannerComponentViewHolder` from `discovery/home_component`

![](res/Banner.png)

#### Products Recommendation

using `MixTopComponentViewHolder` from `discovery/home_component`

![](res/Frame%203126176.png)

#### Flash Sale

using `MixLeftComponentViewHolder` from `discovery/home_component`

![](res/Kejar%20Diskon.png)

#### Brand Recommendation

using `MixLeftComponentViewHolder` from `discovery/home_component`

![](res/Product%20Campaign.png)

#### Dilayani Tokopedia Recommendation For You

Currently this widget part of page of Dilayani Tokopedia inside module `logistic/dilayanitokopedia`

![](res/Screenshot%202023-02-23%20at%2010.19.49.png)

## Navigation

tokopedia://dilayani-tokopedia → open Dilayani Tokopedia Homepage

Phase 1 Dilayani Tokopedia Homepage development will have entry point from existing Dilayani Tokopedia. 

## GQL List



| **GQL Name** | **Documentation Link** | **Description** |
| --- | --- | --- |
| getHomeChannelV2 | [HPB/Home - [API:GQL] GraphQL getHomeChannelV2](https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2043906993)  | Get widgets |
| getHomeRecommendationProductV2 | [HPB/Home - [API:GQL] GraphQL getHomeRecommendationProductV2](https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053933208)  | Get recommendation |
| getHomeIconV2 | [HPB/Home - [API:GQL] GraphQL getHomeIconV2](https://tokopedia.atlassian.net/wiki/spaces/HP/pages/2053540091)  | Get Icons for Anchor Tab |

# Flow Diagram

![](res/Untitled%20Diagram.drawio%20%282%29.png)



---

## Useful Links

- [Figma](https://figma.com/)
- [PRD Dilayani Tokopedia Homepage](https://docs.google.com/document/d/19-ux-cIMT37gBq0hTbuAGLlS_0Y7d88XQ739abGNu48/edit#heading=h.2bxh3fr1rhw)
- [DT Homepage Tracker](https://www.figma.com/file/hakm6u0F9qxSFnXXvbeJU3/DT-Homepage-Revamp---%5BM%5D?node-id=97%3A59759&t=Gejb81Xfs0xzxwM3-0)
- <https://docs.google.com/spreadsheets/d/1jHH4R_Yry1ZYm5JlrY6B3L25D0rVkT3Ibx3oXpxdhgU/edit#gid=1231583495>
- <https://tokopedia.atlassian.net/wiki/spaces/PA/pages/967945643/Navigation+Toolbar#Toolbar-title-setup>

## Action Items

- Update this doc about the widgets
- update logic hit api sequentially including changes of flow
- update GetHomeRecommendationQuery, GetHomeLayoutDataQuery, and GetAnchorTabQuery to use annotation @GqlQuery. see Example : `CustomProductLogisticUseCase`
- continue phase 2 development