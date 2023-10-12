---
title: "Catalog Library"
labels:
- catalog-library
---


| **Status** |  <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Mohit Singh](https://tokopedia.atlassian.net/wiki/people/5ff3fa2244065f013f9f1eb9?ref=confluence) [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence)  |
| Product Manager | [Karenia Emeralda Arnetha](https://tokopedia.atlassian.net/wiki/people/6170b6eb3e3753006fa4d8e4?ref=confluence)  |
| Team | [Darth](https://tokopedia.atlassian.net/people/team/8c90de56-d4f1-45a7-9021-bd87c4ea9ce2) ([Mohit Singh](https://tokopedia.atlassian.net/wiki/people/5ff3fa2244065f013f9f1eb9?ref=confluence) [Zishan Rashid](https://tokopedia.atlassian.net/wiki/people/5c53e2323290dd17112962f7?ref=confluence) ) |
| Release date | 26 Apr 2023 / <!--start status:GREY-->MA-3.218<!--end status-->  |
| Module type<br/> | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Module Location | `features/category/catalog_library` |
| PRD | <https://docs.google.com/document/d/1p_dmNREfKvgzLZvuLy0ktITsf2bQOXlJk7Af6OPPVMw/edit>  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:26 Apr 2023 (MA-3.218)-->
###### *Catalog Phase 1 and Phase 2*
<!--end expand-->

## Overview

Catalog is a collection of products that has an identical brand, type, and specifications (a SKU) from various brands to provide users with useful information to make a more confident purchase decision within Tokopedia. These are the problems we try to solve by Catalog Library:

1. User has limited access to limited catalog pages in Tokopedia: entry point to catalog depends on search autocomplete, Category Landing Page (CLP)

, and carousel on Search Result Page (SRP)

. The search autocomplete accounts for >60% of catalog sessions, however, the entry point is intermittent depending on 1) keywords searched by users and 2) other ongoing campaigns occupying the autocomplete, which could replace the catalog’s position on autocomplete anytime. Moreover, CLP and search carousel cannot accommodate featuring every single catalog on their page due to real estate problems.
2. User doesn’t know which product has catalog and which product does not: the catalog team decides on which products to cover based on which products/categories have the highest number of order/TIV. The SKU selection therefore is non sequential, i.e. catalog does not cover every single SKUs in handphones category. Without knowing the available catalog, users may feel lost when trying to compare products.
3. User cannot access the complete list of catalog/navigate seamlessly through different categories available on catalog: catalog does not have a homepage to list all the available catalog pages or enable browsing within catalog. Users might find a hard time to navigate from one page to another, making their experience in catalog not optimal.

To help answer those problems, we propose to create a catalog indexing page or a Catalog Library. Catalog Library provides structured, indexed catalog pages for users to explore. Ideally, this initiative is aimed to fix navigation funnels of Catalog in Tokopedia Apps so that Catalog Library will be the final landing page for every catalog entrypoint. 

### Project Description

Catalog Library provides a way to view all the listed catalogs in tokopedia. User can discover using specially promoted and relevant catalogs or my kategori. 

**Home Landing Page** which contains Special, Relevant, Brands , 9 Kategori and Products components 

**Kategori Lihat Semua

 Page** lets select the specific kategori for viewing respective catalogs and products.

**Category Landing Page** lets you view curated top five, relevant, most viral and other products as well of that category

**Brand Popular Page** displays the most popular brand with their most popular 3 catalogs.

**Brand Landing Page** displays the products of that brand and lets you filter on the basis of category

## Tech Stack

- Visitor Pattern: Components from BE

## Flow Diagram



| **API** | **Notes** | **Type** |
| --- | --- | --- |
| [GQL - Get Catalog Category Special](/wiki/spaces/CT/pages/1965065065/GQL+-+Get+Catalog+Category+Special)  | List of special catalog  | GQL |
| [GQL - Get Relevant Catalog](/wiki/spaces/CT/pages/1964541654/GQL+-+Get+Relevant+Catalog)  | List of catalog for Relevant Component  | GQL |
| [GQL - Get Catalog Brand Popular](/wiki/spaces/CT/pages/1965228444/GQL+-+Get+Catalog+Brand+Popular)  | Get Brands for Brand Popular page | GQL |
| [GQL - Get Catalog List](/wiki/spaces/CT/pages/1964967507/GQL+-+Get+Catalog+List)  | Get catalog list in Home Landing Page and Brand Landing Page | GQL |
| [GQL - Get Categories By Brand](/wiki/spaces/CT/pages/2041940448/GQL+-+Get+Categories+By+Brand)  | Get categories by brand name | GQL |

## Navigation

![](res/Screenshot%202023-05-24%20at%208.39.12%20PM.png)

![](res/Screenshot%202023-05-24%20at%208.39.25%20PM.png)

![](res/Screenshot%202023-05-24%20at%208.39.39%20PM.png)

![](res/Screenshot%202023-05-24%20at%208.40.01%20PM.png)

![](res/Screenshot%202023-05-24%20at%208.42.21%20PM.png)

## How-to



```
RouteManager.route(context,appLink)
```

Applink List

1. Catalog Library Home Page: `tokopedia://catalog_library`
2. Catalog Library Kategori Page: `tokopedia://catalog_library/kategori`
3. Catalog Library Landing Page: `tokopedia://catalog_library/kategori/{categoryId}/{categoryName}`
4. Catalog Library Popular Brand List Page: `tokopedia://catalog_library/popular-brand`
5. Catalog Library Brand Landing Page: `tokopedia://catalog_library/{brandId}/{brandName}`



| **Param Name** | **Mandatory** | **Description** | **Example** |
| --- | --- | --- | --- |
| `categoryName` | No | name of the category for title on screen, if not then will fetch from api | `audio` |
| `catergoryId` | Yes | id of category to fetch data | `26` |



---

## Useful Links

- [Figma](https://www.figma.com/file/L6EpcZx8yOw6hPr0z1s9Fc/%5BCatalog%5D-Catalog-Library?node-id=264%3A25708)

