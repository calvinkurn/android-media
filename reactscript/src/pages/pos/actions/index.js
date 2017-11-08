export * from './product'
export * from './shop'
export * from './cart'
export * from './checkout'
export * from './bank'
export * from './payment'
export * from './invoice'



//  ==================== Clear the Redux ===================== //
export const RELOAD_STATE = 'RELOAD_STATE'
export const reloadState = (state, action) => {
  return {
    type: RELOAD_STATE
  }
}