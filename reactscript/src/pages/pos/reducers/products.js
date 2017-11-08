import {
    FETCH_PRODUCTS,
    PULL_TO_REFRESH,
    RESET_PRODUCT_LIST,
    ON_SUBMIT_FETCH_SEARCH_PRODUCT,
} from '../actions/index'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import { differenceBy } from 'lodash'



export const products = (state = {
    items: [],
    pagination: {
      start: 0,
      rows: 25,
    },
    isFetching: false,
    refreshing: false,
    canLoadMore: false
  }, action) => {
    switch (action.type) {
      case `${FETCH_PRODUCTS}_${PENDING}`:
        return {
          ...state,
          isFetching: true,
        }
      case `${FETCH_PRODUCTS}_${FULFILLED}`:
        const products = action.payload.data.list || []
        const newItems = differenceBy(products, state.items, 'id')
        const items = [...state.items, ...newItems]
        const nextUrl = action.payload.data.paging.uri_next
        const pagination = {
          ...state.pagination,
          start: items.length
        }
        return {
          items,
          pagination,
          isFetching: false,
          refreshing: false,
          canLoadMore: nextUrl ? true : false,
        }
      case `${FETCH_PRODUCTS}_${REJECTED}`:
        return {
          ...state,
          isFetching: false,
          refreshing: false,
        }
      case PULL_TO_REFRESH:
        return {
          items: [],
          pagination: {
            start: 0,
            rows: 25,
          },
          isFetching: false,
          refreshing: true
        }
      case RESET_PRODUCT_LIST:
        return {
          ...state,
          items: [],
          pagination: {
            start: 0,
            rows: 25,
          },
        }
      case `${ON_SUBMIT_FETCH_SEARCH_PRODUCT}_${FULFILLED}`:
        console.log(ON_SUBMIT_FETCH_SEARCH_PRODUCT)
        return {
          ...state,
          items: action.payload.data.data.products
        }
      default:
        return state
    }
  }