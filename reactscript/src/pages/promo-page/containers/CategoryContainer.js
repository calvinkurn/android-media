import { connect } from 'react-redux'
import Categories from '../components/Categories'
import { fetchCategories } from '../actions/index'

const mapStateToProps = (state, ownProps) => ({
  dataSlug: ownProps.dataSlug,
  categories: state.categories,
  navigation: ownProps.navigation,
  termsConditions: ownProps.termsConditions,
})

const mapDispatchToProps = (dispatch, ownProps) => ({
  getCategories: (offset, limit) => {
    dispatch(fetchCategories(ownProps.dataSlug, offset, limit))
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(Categories)