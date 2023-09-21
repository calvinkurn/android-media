---
title: "Single Review Form (BottomSheet)"
labels:
- review-bottomsheet
- buyer
- single-review-form
- review
- review-form
- write-review
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| Contributors | [Yusuf Hendrawan](https://tokopedia.atlassian.net/wiki/people/5df336f3f4ab290ecfc64169?ref=confluence) |
| Product Manager | [Mia Renauly](https://tokopedia.atlassian.net/wiki/people/5dd5ff00a20e0c0e9ef6f8a3?ref=confluence) [I Komang Ananta Aryadinata](https://tokopedia.atlassian.net/wiki/people/6293ecf71a2bdf0070936aeb?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e?ref=directory&src=peopleMenu) |
| Release date |  |
| Module type |  <!--start status:YELLOW-->FEATURE<!--end status--> |
| Product PRD | [Review Writing Form Audit](/wiki/spaces/PDP/pages/1225552051/Review+Writing+Form+Audit)  |
| Package Location | `com.tokopedia.review.feature.createreputation` |
| Fragment Class | `CreateReviewBottomSheet` |

- [Overview](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2327871489/Single+Review+Form+BottomSheet#Overview)
- [Flowchart](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2327871489/Single+Review+Form+BottomSheet#Flowchart)
- [Navigation](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2327871489/Single+Review+Form+BottomSheet#Navigation)
- [GQL](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2327871489/Single+Review+Form+BottomSheet#GQL)
- [Useful Links](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2327871489/Single+Review+Form+BottomSheet#Useful-Links)

# Overview

The single review form is a page built using a BottomSheet and placed in a transparent activity. The purpose of using an activity to wrap the bottom sheet was to make the page accessible through an app link. On this page, the user can submit a review for a single product. The form structure is static with some static data and some dynamic data controlled by the backend (ex: template list, bad rating category list, etc).



| ![](res/review%20good%20rating.png)<br/> | ![](res/bad%20rating.png)<br/> | ![](res/review%20good%20rating%20with%20attachment.png)<br/> |
| --- | --- | --- |

# Flowchart

![](res/Single%20review%20form.drawio%20%281%29.png)

![](res/Submit%20review%20flow.drawio.png)

# Navigation



| External Applink | `tokopedia://product-review/create/{reputation_id}/{product_id}?rating=[1-5]&feedbackId={feedback_id}&source={utm_source}` → old app link with `source` parameter instead of `utm_source`, please use app link below instead (using `utm_source`)`tokopedia://product-review/create/{reputation_id}/{product_id}?rating=[1-5]&feedbackId={feedback_id}&utm_source={utm_source}` |
| --- | --- |
| Internal Applink  | `tokopedia-android-internal://marketplace//product-review/create/{reputation_id}/{product_id}?rating=[1-5]&feedbackId={feedback_id}&utm_source={utm_source}` |

# GQL

There are 2 groups of GQL queries that will be executed when the page is opened:

- Mandatory → The productrevGetFormV2 and productrevGetBadRatingCategory are required and needed to render the whole form.
- Optional → The productrevIncentiveOvo and productrevGetListPersonalizedReviewTemplate is an optional query that needed to be sent but does not necessarily need to return a successful response.

GQL queries that will be executed when the user submits the review:

- `productrevIncentiveOvo` → This query is used to check whether the user has any pending incentive eligible review
- `productrevSubmitReviewV2` → This query is used to submit the inserted review data
- `productrevGetPostSubmitBottomSheet` → This query is used to determine whether we need to show a toaster or bottom sheet after successfully submitting the review data



| **Query** | **Description** | **Documentation** |
| --- | --- | --- |
| `query productrevGetFormV2`  | A mandatory query for getting the data required to render the form (ex: TextArea placeholder, product info, etc).If this query is failing then the page must immediately dismissed and closed and show the toaster on the entrypoint. | [productrevGetFormV2](/wiki/spaces/PDP/pages/2236908215/productrevGetFormV2)  |
| `query productrevGetBadRatingCategory`  | A mandatory query for getting the data required to render the bad rating category reasons when the user select rating that is below the good rating threshold.If this query is failing then the page must immediately dismissed and closed and show the toaster on the entrypoint. | [productrevGetBadRatingCategory](/wiki/spaces/PDP/pages/1799979153/productrevGetBadRatingCategory)  |
| `query productrevGetListPersonalizedReviewTemplate`  | An optional query for getting the data required to render the template list.If this query is failing then the page must not show an error state and not render the template list even though that the review is actually have a template list data on the backend but not successfully retrieved by the app. | [Get Personalized Review Template - Tech Docs](/wiki/spaces/PDP/pages/1415056246/Get+Personalized+Review+Template+-+Tech+Docs)  |
| `query productrevIncentiveOvo` | An optional query for getting the data required to render the incentive related UI (ex: incentive ticker).If this query is failing then the page must not show an error state and not render the incentive related UI even though that the review is actually eligible for incentive. | [Review Ovo Incentive](/wiki/spaces/PDP/pages/928483891/Review+Ovo+Incentive)  |
| `query productrevSubmitReviewV2` | A query used for sending the inserted review data.If this query is failing or receive a non-success result then the page must show an error toaster. | [productrevSubmitReview](/wiki/spaces/PDP/pages/1157379276/productrevSubmitReview)  |
| `query productrevGetPostSubmitBottomSheet` | A query used for determining the post submit flow (whether the app need to show a toaster on the entrypoint or open a post submit bottom sheet).If this query is failing then the page must not show any errors and show a success toaster indicating that the submission is successful. | [Post Submit Bottom Sheet](/wiki/spaces/PDP/pages/1855012369/Post+Submit+Bottom+Sheet)  |

# Useful Links



| Figma | <https://www.figma.com/file/XW1UT4jOMkr2AlYghe2cOk/Review-Master?type=design&node-id=2-111&mode=design&t=ItQ1X6ZoLD0xJwyq-0>  |
| --- | --- |
| Trackers | <https://mynakama.tokopedia.com/datatracker/requestdetail/view/1377>  |

