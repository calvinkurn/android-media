import { combineReducers } from 'redux'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import { FETCH_PRODUCTS, FETCH_ETALASE, PULL_TO_REFRESH, ON_ETALASE_CHANGE, RESET_PRODUCT_LIST } from '../actions/index'

const products = (state = {
  items: [],
  pagination: {
    start: 0,
    rows: 25,
  },
  isFetching: false,
  refreshing: false,
  canLoadMore: false,
}, action) => {
  switch (action.type) {
    case `${FETCH_PRODUCTS}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_PRODUCTS}_${FULFILLED}`:
      const products = action.payload.data.data.products || []
      const items = [...state.items, ...products]
      const nextUrl = action.payload.data.data.paging.uri_next
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
    default:
      return state
  }
}

const etalase = (state = {
  items: [{
    id: '0',
    name: 'All Products',
    alias: 'all_products'
  }],
  selected: '0'
}, action) => {
  switch (action.type) {
    case `${FETCH_ETALASE}_${PENDING}`:
      return state
    case `${FETCH_ETALASE}_${FULFILLED}`:
      const etalases = action.payload.data.data.map(e => ({
        id: e.menu_id,
        name: e.menu_name,
        alias: e.menu_alias,
      }))
      console.log(etalases)

      return {
        ...state,
        items: [{
          id: '0',
          name: 'All Products',
          alias: 'all_products'
        }, ...etalases],
      }
    case `${FETCH_ETALASE}_${REJECTED}`:
      return state
    case ON_ETALASE_CHANGE:
      return {
        ...state,
        selected: action.payload,
      }
    default:
      return state
  }
}

const rootReducer = combineReducers({
  products,
  etalase
})

export default rootReducer