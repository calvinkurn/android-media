import { connect } from 'react-redux'
import Categories from '../components/Categories'
import { fetchCategories, getApplink } from '../actions/index'

const mapStateToProps = (state, ownProps) => {
  return {
    dataSlug: ownProps.dataSlug,
    categories: state.categories,
    navigation: ownProps.navigation,
    termsConditions: ownProps.termsConditions,
    applinkEnv: state.applink,
  }
}

const mapDispatchToProps = (dispatch, ownProps) => ({
  getCategories: (offset, limit) => {
    dispatch(fetchCategories(ownProps.dataSlug, offset, limit))
  },
  getApplinkEnv: () => {
    dispatch(getApplink())
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(Categories)